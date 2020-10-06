package com.rishabhmatharoo.blacklight.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model.DataClass;
import com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model.Message;
import com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model.PayloadData;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;

public class PayLoadDialog extends Dialog {
    private Activity main;
    public PayLoadDialog(Activity activity){
        super(activity);
        this.main=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.payloaddialoglayout);

        try {
            ViewGroup.LayoutParams params = getWindow().getAttributes();
             if (params != null) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                //Log.d("gravitytop",""+Gravity.CENTER);
                //getWindow().setGravity(Gravity.TOP);
                getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView payloadvalue=findViewById(R.id.payloadvalues);
        try {
            if (!SharedPreferenceClass.getInstance(getContext()).getDataPayloadValue().isEmpty()) {
                //PayloadData payloadData = new Gson().fromJson(SharedPreferenceClass.getInstance(getContext()).getDataPayloadValue(), PayloadData.class);
               // Log.d("PayLoad", payloadData.getName());
               DataClass message=new Gson().fromJson(SharedPreferenceClass.getInstance(getContext()).getDataPayloadValue(),DataClass.class);
                payloadvalue.setText("PayLoad Data Name:" + message.Name + "\n PayLoad Data Msg:" + message.Msg + "\n PayLoad Data MsgType:" + message.msgType);
                SharedPreferenceClass.getInstance(getContext()).setDataPayload("");
                SharedPreferenceClass.getInstance(getContext()).setDataPayloadboolean(false);
            }
        }catch (Exception e){

        }
    }

}
