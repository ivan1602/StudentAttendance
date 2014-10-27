package com.example.ivan.studentattendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ListMySubjects extends Activity implements View.OnClickListener {

    TextView nome;
    String profNumber ="";
    ListView listView;
    JSONArray aulasArray;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_my_subjects);
        nome = (TextView) findViewById(R.id.textView1);
        listView = (ListView)findViewById(R.id.subject_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("cancel") != null)
        {
            nome.setText("Cancelled");
        } else
        {
            profNumber = bundle.getString("professorNumber");
            //nome.setText(profNumber);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //String subject = ((TextView) view).getText().toString();
                Intent i = new Intent(getApplicationContext(),Classes.class);
              //  i.putExtra("subject", subject);
                i.putExtra("broj", profNumber);
                    try {
                        i.putExtra("codtur", aulasArray.getJSONObject(position).getString("codtur"));
                        i.putExtra("Descricao", aulasArray.getJSONObject(position).getString("descricao"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                startActivity(i);
            }
        });
       new task().execute();
    }

   class task extends AsyncTask<String, String, String>
    {
        private ProgressDialog progressDialog = new ProgressDialog(ListMySubjects.this);

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Geting data");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    task.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            String url_select = "http://200.230.71.5:8182/ProfAula-1.0/DocentesDisciplina/docdis/"+profNumber;
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
                Log.e("rezultat", html);
                return html;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result)
        {
            try
            {
                    JSONObject mainObjects = new JSONObject(result);
                    Object json = new JSONTokener(mainObjects.getString("docentesDisciplinas")).nextValue();
                    if (json instanceof JSONArray) {
                        Log.e("result", "prazno:" + result);
                        JSONObject mainObject = new JSONObject(result);
                        aulasArray = mainObject.getJSONArray("docentesDisciplinas");
                        ArrayList<String> data = new ArrayList<String>();
                        String subList = "";
                        for (int i = 0; i < aulasArray.length(); i++) {
                            JSONObject aulasObject = aulasArray.getJSONObject(i);
                            //get an output on the screen
                            String pod1 = aulasObject.getString("professor");
                            if (pod1.equalsIgnoreCase(profNumber)) {
                                subList = "Nome curso:" + aulasObject.getString("nome_curso");
                                subList += " \n" + "Descricao:"  + aulasObject.getString("descricao");
                                subList += " \n" + "Codtur:" + aulasObject.getString("codtur");//potrebno proslijediti u sljedeću aktivnost
                                subList += " \n" + "Turma:" + aulasObject.getString("turma");
                                subList += " \n" + "Periodo:" + aulasObject.getString("periodo");
                                data.add(subList);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListMySubjects.this,
                                android.R.layout.simple_list_item_1, data.toArray(new String[data.size()]));
                        listView.setAdapter(adapter);

                    } else {
                        Log.e("result", "prazno:" + result);
                        JSONObject mainObject = new JSONObject(result);
                        JSONObject aulasArray = mainObject.getJSONObject("docentesDisciplinas");
                        ArrayList<String> data = new ArrayList<String>();
                        String subList = "";
                        //get an output on the screen
                        String pod1 = aulasArray.getString("professor");
                        if (pod1.equalsIgnoreCase(profNumber)) {
                            subList = "Nome curso:" + aulasArray.getString("nome_curso");
                            subList += " \n" + "Descricao:"  + aulasArray.getString("descricao");
                            subList += " \n" + "Codtur:" + aulasArray.getString("codtur");//potrebno proslijediti u sljedeću aktivnost
                            subList += " \n" + "Turma:" + aulasArray.getString("turma");
                            subList += " \n" + "Periodo:" + aulasArray.getString("periodo");
                            data.add(subList);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListMySubjects.this,
                               android.R.layout.simple_list_item_1, data.toArray(new String[data.size()]));
                        listView.setAdapter(adapter);
                    }
                    this.progressDialog.dismiss();
            }
            catch (Exception e)
             {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
             }
        }
    }
        @Override
    public void onClick(View view) {

    }
}
