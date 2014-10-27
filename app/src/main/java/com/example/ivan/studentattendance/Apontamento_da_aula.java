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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Apontamento_da_aula extends Activity implements AdapterView.OnItemSelectedListener
{
    String codTur ="";
    String prof_id="";
    String subject="";
    ArrayList<Student> nomes = new ArrayList<Student>();
    Spinner planejamento;
    AdapterClass adapterClass ;
    Button aply;
    ListView listViewAntendance;
    RadioGroup radGrp;
    EditText apont;
    String falta="0";
    String plano_id="";
    ArrayList<String> data = new ArrayList<String>();
    JSONArray planoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apontamento_da_aula);
        Intent i = getIntent();
        codTur = i.getStringExtra("codtur");
        prof_id = i.getStringExtra("professor_id");
        subject = i.getStringExtra("id");
        listViewAntendance = (ListView)findViewById(R.id.list);
        apont = (EditText)findViewById(R.id.apontamento);
        planejamento = (Spinner) findViewById(R.id.planejamento);
        new task().execute();
        new spinerData().execute();
        aply = (Button)findViewById(R.id.button);
        planejamento.setOnItemSelectedListener(this);
        aply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(getPostData().toString());
                new HttpAsyncTask().execute();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String label="";
        label = adapterView.getItemAtPosition(position).toString();
        try {
            plano_id= planoArray.getJSONObject(position).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}


    class task extends AsyncTask<String, String,String >
    {
        private ProgressDialog progressDialog = new ProgressDialog(Apontamento_da_aula.this);

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
            String url_select = "http://200.230.71.5:8182/ProfAula-1.0/proff/alunos/"+ subject;
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
                    String subList = "";
                    String id_List="";
                    for (int i = 0; i < aulasArray.length(); i++) {
                        JSONObject aulasObject = null;
                        aulasObject = aulasArray.getJSONObject(i);
                        subList = aulasObject.getString("nome");
                        id_List = aulasObject.getString("id");
                        nomes.add(new Student(subList,0,id_List));
                    }
                    adapterClass = new AdapterClass(Apontamento_da_aula.this, nomes);
                    listViewAntendance.setAdapter(adapterClass);
                    this.progressDialog.dismiss();
                }
                else
                {
                    this.progressDialog.dismiss();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

    class spinerData extends AsyncTask<String, String,String >
    {

        @Override
        protected String doInBackground(String... params) {
            String url_select = "http://200.230.71.5:8182/ProfAula-1.0/plano/"+prof_id +"/"+codTur;
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
                while ((line = reader.readLine()) != null)
                {
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
                    planoArray = mainObject.getJSONArray("plano");

                    String subList = "";
                    for (int i = 0; i < planoArray.length(); i++) {
                        JSONObject aulasObject = null;
                        aulasObject = planoArray.getJSONObject(i);
                        //get an output on the screen
                        subList = aulasObject.getString("descricao");
                        data.add(subList);
                    }
                    populateSpinner();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "poruka!!! =)",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            lables.add(data.get(i));

        }
        System.out.println("labels" + lables);
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        planejamento.setAdapter(spinnerAdapter);
    }

    private JSONObject getPostData ()
    {
        JSONObject main = new JSONObject();

        try {
            JSONObject apotamento = new JSONObject();
            JSONObject planejamento = new JSONObject();
            JSONObject codtur = new JSONObject();
            JSONObject codprofessor = new JSONObject();
            JSONObject chamada_id = new JSONObject();
            apotamento.accumulate("apontamento" , apont.getText().toString() );
            //planejamento.accumulate("planejamento",this.planejamento.getSelectedItem().toString());
            planejamento.accumulate("planejamento", plano_id);
            codtur.accumulate("codtur",codTur);
            codprofessor.accumulate("cod_professor",prof_id);
            chamada_id.accumulate("chamada_id", subject);

            JSONArray students = new JSONArray();
            for(int i= 0; i<adapterClass.getCount();i++){
                Student s= adapterClass.getItem(i);
                JSONObject jStudent= new JSONObject();
                jStudent.accumulate("id",s.getId());
                jStudent.accumulate("faltas",s.getFalta());
                students.put(jStudent);
            }
            main.accumulate("codtur",codtur);
            main.accumulate("apontamento", apotamento);
            main.accumulate("planejamento", planejamento);
            main.accumulate("students", students);
            main.accumulate("cod_professor",codprofessor);
            main.accumulate("chamada_id", chamada_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return main;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
               HttpPost httpPost = new HttpPost("http://200.230.71.5:8182/ProfAula-1.0/faltas/inserir");
               // HttpPost httpPost = new HttpPost("http://posttestserver.com/post.php?dir=test1");

                String json = getPostData().toString();
                System.out.println("json result:" +json);


                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity
                StringEntity se = new StringEntity(json);

                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));

                // 6. set httpPost Entity
               httpPost.setEntity(se);

                // 7. Set some headers to inform server about the type of the content
                //httpPost.setHeader("Accept", "application/json");
                //httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
               inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(httpResponse != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            // 11. return result
            return result;
        }
        private String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!"+result, Toast.LENGTH_LONG).show();
            System.out.println("json result" +result);

        }
    }

}
