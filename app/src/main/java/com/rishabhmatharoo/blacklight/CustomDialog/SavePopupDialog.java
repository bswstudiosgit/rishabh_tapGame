package com.rishabhmatharoo.blacklight.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;

import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Interfaces.GameViewInterface;
import com.rishabhmatharoo.blacklight.Interfaces.PopupCallBackFragmentInterface;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;

public class SavePopupDialog extends Dialog implements android.view.View.OnClickListener{
    public Activity main;
    public Button yes,no;
    public boolean dismissvar=false;
    String tag="gameview";
    String tag2="Resume";
    CardView cardView;
    PopupCallBackFragmentInterface popupCallBackFragmentInterface;
    GameViewInterface gameViewInterface;
    private int certainbottomvalue=5,nativeadRefreshingTime=120000;
    private Handler refreshnativead;
    //Passing dependency injection of handler in the constructor
    public SavePopupDialog(Activity activity,Handler refreshhandler){
        super(activity);
        this.main=activity;
        this.refreshnativead=refreshhandler;
    }
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.savingpopup);

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
        //ExitDialog exitDialog=new ExitDialog(main);
        //exitDialog.show();
        no=(Button)findViewById(R.id.cancelbutton);
        yes=(Button)findViewById(R.id.savebutton);
        no.setOnClickListener(this);
        yes.setOnClickListener(this);
        cardView=findViewById(R.id.ad_container2);
        ImageView close=(ImageView)findViewById(R.id.closebtn);
        close.setOnClickListener(this);
        showNativeAdDialog();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cancelbutton:
                SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.savegamescore,0);
                SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isSave,false);
                //SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isPopupActive,false);
                //Utilclass.isSavePopActive =false;
                popupCallBackFragmentInterface.onCallBack(tag);
                refreshNativeAd();
                dismiss();

                break;
            case R.id.savebutton:
                SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isSave,true);
                Toast.makeText(getContext(),"Data Saved",Toast.LENGTH_SHORT).show();
                //SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isPopupActive,false);
                //Utilclass.isSavePopActive =false;
                dismiss();
                refreshNativeAd();
                popupCallBackFragmentInterface.onCallBack(tag);

                break;
            case R.id.closebtn:
                //dismissvar =0 true;
                //SharedPreferenceClass.getInstance(getContext()).
                // writeBoolean(SharedPreferenceClass.isPopupActive,false);
                //Utilclass.isSavePopActive =false;
                dismiss();
                refreshNativeAd();
                gameViewInterface.runHandler();
                break;
        }


    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
        //dismissvar = !dismissvar;
        if (dismissvar) {
            dismissvar = false;
        } else {
            dismissvar = true;
            dismiss();
            gameViewInterface.runHandler();

        }
    }



    public void setPopupCallBackFragmentInterface(PopupCallBackFragmentInterface popupCallBackFragmentInterface){
        this.popupCallBackFragmentInterface=popupCallBackFragmentInterface;
    }
    public void setGameViewInterFace(GameViewInterface gameViewInterFace1){
        this.gameViewInterface=gameViewInterFace1;
    }
    private void showNativeAdDialog(){

        //Here we will show the Ad
        AdMobHandler.getInstance(main).showNativeAd(cardView);
    }
    public void refreshNativeAd(){
        /*
        refreshnativead.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdMobHandler.getInstance(main).loadNativeAd();
            }
        },nativeadRefreshingTime);

         */
        AdMobHandler.getInstance(main).loadNativeAd();
    }



}
