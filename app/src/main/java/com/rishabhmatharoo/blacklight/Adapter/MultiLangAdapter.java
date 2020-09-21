package com.rishabhmatharoo.blacklight.Adapter;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rishabhmatharoo.blacklight.Interfaces.RecylerViewCallBack;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;

import java.util.ArrayList;
import java.util.Locale;

public class MultiLangAdapter extends RecyclerView.Adapter<MultiLangAdapter.ViewHolder> {
    private ArrayList<String> listdata=new ArrayList<>();
    private Activity activity;
    int prevposition=0;
    RecylerViewCallBack callBack;

    public void setCallBack(RecylerViewCallBack callBack1){
        this.callBack=callBack1;
    }

    public MultiLangAdapter(ArrayList<String> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recylerviewlayout_multlang, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.radiobutton.setChecked(false);


        if(SharedPreferenceClass.getInstance(activity).read(SharedPreferenceClass.language)==position){
                //holder.imageView.setImageResource(R.drawable.check_box);
                holder.radiobutton.setChecked(true);
                prevposition=position;
            }

            holder.textView.setText(listdata.get(position));

            holder.radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferenceClass.getInstance(activity).write(SharedPreferenceClass.language,position);
                    changelanguage(position);
                    holder.radiobutton.setChecked(true);
                    prevposition=position;

                }
            });



    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RadioButton radiobutton;
        public TextView textView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.radiobutton = (RadioButton) itemView.findViewById(R.id.unticked);
            this.textView = (TextView) itemView.findViewById(R.id.languagetype);
            this.linearLayout=(LinearLayout)itemView.findViewById(R.id.multilanglayout);


        }
    }
    public void changelanguage(int id){
        String language="";
        if(id==0){
            language="en";
        }else if(id==1){
            language="hi";
        }else if(id==2){
            language="ar";
        }else if(id==3){
            language="fr";
        }else if(id==4){
            language="de";
        }else if(id==5){
            language="ja";
        }
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        activity.getResources().updateConfiguration(configuration,activity.getResources().getDisplayMetrics());
        callBack.changecallback();
    }

}



