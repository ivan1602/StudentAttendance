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


public class Classes extends Activity {
    TextView des,professor;
    String codTur ="";
    String prof_id="";
    String descricao="";
    ListView class_list;
    JSONArray aulasArray;
    JSONObject chamadaObject;
    JSONObject aulaArray;
    String pod1,pod2;
    String aulas="";
    String aulas2="";
    JSONObject mainObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        class_list= (ListView) findViewById(R.id.class_list);
        des = (TextView) findViewById(R.id.subject);
        professor = (TextView) findViewById(R.id.proff);
        Intent i = getIntent();
        codTur = i.getStringExtra("codtur");
        prof_id = i.getStringExtra("broj");
        descricao=i.getStringExtra("Descricao");
        // displaying selected product name
        des.setText(descricao);
        //professor.setText(prof_id);
        System.out.println(des);
        System.out.println(professor);
        new Aulas().execute();
        class_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                try {
                    aulas = aulasArray.getJSONObject(position).getString("chamada");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (aulas.equalsIgnoreCase("N")||aulas2.equalsIgnoreCase("N"))
                {
                    Intent i = new Intent(getApplicationContext(), Apontamento_da_aula.class);

                    i.putExtra("codtur",codTur);
                    i.putExtra("professor_id",prof_id);
                    try {
                        i.putExtra("id", aulasArray.getJSONObject(position).getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    startActivity(i);

            }
                else
                {
                    Intent i = new Intent(getApplicationContext(), Old_classes.class);
                    i.putExtra("codtur",codTur);
                    i.putExtra("professor_id",prof_id);
                    try {
                        i.putExtra("id", aulasArray.getJSONObject(position).getString("id"));
                        i.putExtra("apontamento", aulasArray.getJSONObject(position).getString("apontamento"));
                        i.putExtra("planejamento", aulasArray.getJSONObject(position).getString("id_apontamento_planejamento"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(i);
                }
                }


        });


    }

    class Aulas extends AsyncTask<String, String, String>
    {
        private ProgressDialog progressDialog = new ProgressDialog(Classes.this);

        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Geting data");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Aulas.this.cancel(true);
                }
            });
        }
        @Override
        protected String doInBackground(String... strings) {
            String url_select = "http://200.230.71.5:8182/ProfAula-1.0/prof/post/"+codTur +"/"+prof_id;
            System.out.println("url select is :" + url_select);
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
                Object json = new JSONTokener(mainObjects.getString("aulas")).nextValue();
                if (json instanceof JSONArray) {
                    Log.e("result", "prazno:" + result);
                    mainObject = new JSONObject(result);
                    aulasArray = mainObject.getJSONArray("aulas");
                    ArrayList<String> data = new ArrayList<String>();
                    String subList = "";
                    for (int i = 0; i < aulasArray.length(); i++) {
                       chamadaObject = aulasArray.getJSONObject(i);
                        //get an output on the screen
                      pod1 = chamadaObject.getString("chamada");
                        if(pod1.equalsIgnoreCase("N"))
                        {
                            subList = "Data aula:" + chamadaObject.getString("data_aula");
                            subList += " \n" + "Hora aula:" + chamadaObject.getString("hora_aula");
                            data.add(subList);
                        }
                        else
                        {
                            subList = "Data aula:" + chamadaObject.getString("data_aula");
                            subList += " \n" + "Hora aula:" + chamadaObject.getString("hora_aula");
                            subList += "\n" + "Chamada realizada";
                            data.add(subList);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Classes.this,
                            android.R.layout.simple_list_item_1, data.toArray(new String[data.size()]));
                    class_list.setAdapter(adapter);

                } else {
                    Log.e("result", "prazno:" + result);
                    mainObject = new JSONObject(result);
                    aulaArray = mainObject.getJSONObject("aulas");
                    ArrayList<String> data = new ArrayList<String>();
                    String subList = "";
                    //get an output on the screen
                   pod2 = aulaArray.getString("chamada");
                    if(pod2.equalsIgnoreCase("N"))
                    {
                        subList = "Data aula:" + aulaArray.getString("data_aula");
                        subList += " \n" + "Hora aula:" + aulaArray.getString("hora_aula");
                        subList += "\n" + "Chamada realizada";
                        data.add(subList);
                    }
                    else
                    {
                        subList = "Data aula:" + aulaArray.getString("data_aula");
                        subList += " \n" + "Hora aula:" + aulaArray.getString("hora_aula");
                        data.add(subList);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Classes.this,
                            android.R.layout.simple_list_item_1, data.toArray(new String[data.size()]));
                    class_list.setAdapter(adapter);
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
}
