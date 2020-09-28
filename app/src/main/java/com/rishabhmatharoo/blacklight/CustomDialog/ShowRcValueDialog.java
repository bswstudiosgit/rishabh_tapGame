package com.rishabhmatharoo.blacklight.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.RemoteConfig.RemoteColorModel;

public class ShowRcValueDialog extends Dialog {
    public Activity main;
    private TextView handlertime,color1,color2,color3,color4,color5,bgmusic,interstitialfre;
    private String tag1="HandlerTimer: ",tag2="Color1: ",tag3="Color2: ",tag4="Color3: ",tag5="Color4: ",tag6="Color5: ",tag7="A/B Test Bg Music Variable: ",tag8="Interstitial Frequency: ";

    public ShowRcValueDialog(Activity activity){
        super(activity);
        this.main=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rcvaluesdialog);
        try {
            ViewGroup.LayoutParams params = getWindow().getAttributes();
            if (params != null) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handlertime=findViewById(R.id.handlertime);
        color1=findViewById(R.id.color1);
        color2=findViewById(R.id.color2);
        color3=findViewById(R.id.color3);
        color4=findViewById(R.id.color4);
        color5=findViewById(R.id.color5);
        bgmusic=findViewById(R.id.bgmusic);
        interstitialfre=findViewById(R.id.interstitial_freq);
        RemoteColorModel remoteColorModel=new Gson().fromJson(SharedPreferenceClass.getInstance(getContext()).readColorValue(),
                RemoteColorModel.class);
        String handlerTimer=SharedPreferenceClass.getInstance(getContext()).readHandlerTimer();
        handlertime.setText(tag1+handlerTimer);
        color1.setText(tag2+remoteColorModel.getColour1());
        color2.setText(tag3+ remoteColorModel.getColour2());
        color3.setText(tag4+remoteColorModel.getColour3());
        color4.setText(tag5+remoteColorModel.getColour4());
        color5.setText(tag6+remoteColorModel.getColour5());
        bgmusic.setText(tag7+SharedPreferenceClass.getInstance(getContext()).readBgMusic());
        interstitialfre.setText(tag8+SharedPreferenceClass.getInstance(getContext()).readInterstitialFrequency());
    }

}
