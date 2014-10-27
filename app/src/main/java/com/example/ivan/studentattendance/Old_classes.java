package com.example.ivan.studentattendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Old_classes extends Activity implements View.OnClickListener{

    TextView old_classes;
    String subject = "";
    ListView listView;
    String codTur ="";
    String prof_id="";
    String apontamento,planejamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_classes);
        old_classes = (TextView)findViewById(R.id.subject);
        listView = (ListView)findViewById(R.id.classes_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("cancel") != null)
        {
            old_classes.setText("Cancelled");
        } else
        {
            subject = bundle.getString("id");
            codTur = bundle.getString("codtur");
            prof_id = bundle.getString("professor_id");
            apontamento=bundle.getString("apontamento");
            planejamento=bundle.getString("planejamento");
        }
        new getStudent().execute();
        old_classes.setText("Apontamento:" +apontamento + "\n"+ "Planejamento"+ planejamento);
    }

    class getStudent extends AsyncTask<String, String, String>
    {
        private ProgressDialog progressDialog = new ProgressDialog(Old_classes.this);

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Geting data");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    getStudent.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {

            String url_select = "http://200.230.71.5:8182/ProfAula-1.0/proff/alunos/" + subject;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url_select);
                HttpResponse response = null;
                response = client.execute(request);

                String html = "";
                InputStream in = null;

                in = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder str = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
                in.close();
                html = str.toString();
                Log.e("result", html);
                return html;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result) {


            try {
                if(!result.equals("null")) {
                    Log.e("result", "prazno:" + result);
                    JSONObject mainObject = new JSONObject(result);
                    JSONArray aulasArray = mainObject.getJSONArray("alunos");
                    ArrayList<String> data = new ArrayList<String>();
                    String subList = "";

                    for (int i = 0; i < aulasArray.length(); i++) {
                        JSONObject aulasObject = null;
                        aulasObject = aulasArray.getJSONObject(i);
                        //get an output on the screen
                        subList = "Nome:" + aulasObject.getString("nome");
                        if (aulasObject.getString("faltas").equalsIgnoreCase("0")) {
                            subList += " \n" + "Faltas:" + "Nao faltas";
                        } else if (aulasObject.getString("faltas").equalsIgnoreCase("1")) {
                            subList += " \n" + "Faltas:" + "1 falta";
                        } else {
                            subList += " \n" + "Faltas:" + "2 faltas";
                        }
                        data.add(subList);  //kako dodati još informacija u listu ??
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Old_classes.this,
                            android.R.layout.simple_list_item_1, data.toArray(new String[data.size()]));
                    listView.setAdapter(adapter);
                    this.progressDialog.dismiss();
                }
                else
                {
                    this.progressDialog.dismiss();
                    Intent i = new Intent(getApplicationContext(),Apontamento_da_aula.class);
                    i.putExtra("id", subject);
                    startActivity(i);

                }


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }


    @Override
    public void onClick(View view) {

    }
}
