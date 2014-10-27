package com.example.ivan.studentattendance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ProfesorIdentification extends Activity implements View.OnClickListener {

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    Button getName;
    EditText profNumber, password;
    String professor_number="";
    String ime = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_identification);
        profNumber = (EditText) findViewById(R.id.number);
        password=(EditText)findViewById(R.id.pass);
        getName = (Button) findViewById(R.id.button);
        getName.setOnClickListener(this);
        cd = new ConnectionDetector(getApplicationContext());

    }

    @Override
    public void onClick(View view)
    {
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
        // check for Internet status
        if (isInternetPresent)
        {
            // Internet Connection is Present
            // make HTTP requests
            if(profNumber.getText().toString().isEmpty() || password.getText().toString().isEmpty())
            {
                // editText is empty
                Toast.makeText(ProfesorIdentification.this, "You need to enter your ID number and password", Toast.LENGTH_SHORT).show();
            }
            else
            {
                new task().execute();
            }
        }
        else
        {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(ProfesorIdentification.this, "No Internet Connection",
                    "You don't have internet connection, try later.", false);
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting alert dialog icon
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    class task extends AsyncTask<String, String, String>
    {

        private ProgressDialog progressDialog = new ProgressDialog(ProfesorIdentification.this);

        InputStream is = null;
        String result ="";
        protected void onPreExecute (){
            progressDialog.setMessage ("Geting data");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    task.this.cancel(true);
                }
            });
        }
        @Override
        protected String doInBackground(String... params)
        {

            String url_select = "http://200.230.71.5:8182/ProfAula-1.0/Docentes/doc/" + profNumber.getText().toString() +"/"+password.getText().toString();
                System.out.println("String url:"+url_select);
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
                while((line = reader.readLine()) != null)
                {
                    str.append(line);
                }
                in.close();
                html = str.toString();
                Log.e("result",html);
                return html;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String result)
        {
            try {/*
                if(!result.equals("null"))
                {
                    JSONObject mainObjects = new JSONObject(result);
                    Object json = new JSONTokener(mainObjects.getString("docentes")).nextValue();
                    if (json instanceof JSONArray) {
                        Log.e("result", "prazno:" + result);
                        JSONObject mainObject = new JSONObject(result);
                        JSONArray aulasArray = mainObject.getJSONArray("docentes");
                        String imeProf = "";
                        for (int i = 0; i < aulasArray.length(); i++) {
                            JSONObject aulasObject = aulasArray.getJSONObject(i);
                            //get an output on the screen
                            String pod1 = aulasObject.getString("professor");
                            if (pod1.equalsIgnoreCase(profNumber.getText().toString())) {
                                imeProf = aulasObject.getString("nome");
                                ime = imeProf;
                            }

                        }
                        Intent intent = new Intent(ProfesorIdentification.this, Professor.class);
                        intent.putExtra("professorName", ime);
                        intent.putExtra("professorNumber", profNumber.getText().toString());
                        startActivity(intent);
                        this.progressDialog.dismiss();
                    }
                    else
                    {
                        Log.e("result", "prazno:" + result);
                        JSONObject mainObject = new JSONObject(result);
                        JSONObject aulasArray = mainObject.getJSONObject("docentes");
                        String imeProf = "";
                        //get an output on the screen
                        String pod1 = aulasArray.getString("professor");
                        if (pod1.equalsIgnoreCase(profNumber.getText().toString())) {
                            imeProf = aulasArray.getString("nome");
                            ime = imeProf;
                        }
                        Intent intent = new Intent(ProfesorIdentification.this, Professor.class);
                        intent.putExtra("professorName", ime);
                        intent.putExtra("professorNumber", profNumber.getText().toString());
                        startActivity(intent);
                        this.progressDialog.dismiss();
                    }


                }
                else
                {
                    this.progressDialog.dismiss();
                    JSONObject mainObject1 = new JSONObject(result);
                    JSONObject docentes = mainObject1.getJSONObject("result");
                    Toast.makeText(ProfesorIdentification.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    profNumber.setText(null);
                }*/
                JSONObject reader = new JSONObject(result);


                String test =  reader.getString("nome");
                if(!test.equalsIgnoreCase("null"))
                {

                    ime = test;
                    Intent intent = new Intent(ProfesorIdentification.this, Professor.class);
                    intent.putExtra("professorName", ime);
                    intent.putExtra("professorNumber", profNumber.getText().toString());
                    startActivity(intent);
                    this.progressDialog.dismiss();

                }else
                {
                    this.progressDialog.dismiss();
                    Toast.makeText(ProfesorIdentification.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    profNumber.setText(null);
                    password.setText(null);
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }

    }



}
