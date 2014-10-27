package com.example.ivan.studentattendance;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Professor extends Activity implements View.OnClickListener {


    TextView nome, numb;
    String number = "";
    public Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        intent = new Intent();
        intent.setClass(this,ListMySubjects.class);
        Button listAlMySubjects = (Button) findViewById(R.id.listSubjects);
        listAlMySubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Professor.this, ListMySubjects.class);
                i.putExtra("professorNumber", number);
                startActivity(i);
            }
        });
        nome = (TextView) findViewById(R.id.nome);
        numb = (TextView) findViewById(R.id.numb);
        Bundle bundle = getIntent().getExtras();
        String profNumber = bundle.getString("professorNumber");
        String profName = bundle.getString("professorName");
        number = profNumber;
        nome.setText("Welcome professor " + " \n" +profName);

    }

    @Override
    public void onClick(View view)
    {

    }
}