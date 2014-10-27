package com.example.ivan.studentattendance;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ivan on 22/10/2014.
 */
public class AdapterClass extends ArrayAdapter<Student> {
    Context ctx;
    ArrayList<Student> objects;
    String falta="0";

    public AdapterClass(Context context, ArrayList<Student> nomes) {
        super(context,R.layout.custom,nomes);
        ctx=context;
        objects=nomes;
    }
    private class ViewHolder{
        TextView nome;
        RadioButton zero,one,two;
        RadioGroup radGrp;
        public ViewHolder(View v){
            nome=(TextView ) v.findViewById(R.id.textView);
            zero = (RadioButton)v.findViewById(R.id.radio_one0Id);
            one = (RadioButton)v.findViewById(R.id.radio_one5Id);
            two = (RadioButton)v.findViewById(R.id.radio_one10Id);
            radGrp = (RadioGroup)v.findViewById(R.id.radio1Group);
        }
    }
    @Override
    public int getCount() {
        return objects.size();    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            // Creating a view of row.
            convertView = inflater.inflate(R.layout.custom, parent, false);
            holder= new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        final Student p = getItem(position);
        System.out.println(p.getNome());
        holder.nome.setText(p.getNome());

        holder.radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_one0Id:
                        p.setFalta(0);
                        Log.e("rezultat 0", falta);
                        break;
                    case R.id.radio_one5Id:
                        p.setFalta(1);
                        Log.e("rezultat 1", falta);
                        break;
                    case R.id.radio_one10Id:
                        p.setFalta(2);
                        Log.e("rezultat 2", falta);
                        break;
                }
            }
        });
        switch(p.getFalta()){
            case 0:holder.zero.setChecked(true);break;
            case 1:holder.one.setChecked(true);break;
            case 2:holder.two.setChecked(true);break;

        }


        return convertView;
    }

}
