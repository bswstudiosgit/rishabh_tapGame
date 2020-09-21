package com.rishabhmatharoo.blacklight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;

public class ExitDialog extends Dialog {
    private Button refresh,yes,no;
    public Activity main;
    ProgressBar progressBar;
    public ExitDialog(Activity activity){
        super(activity);
        this.main=activity;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_popup_layout);
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
        progressBar=findViewById(R.id.progressbarexit);
        progressBar.setVisibility(View.VISIBLE);

        yes=findViewById(R.id.exitbutton);
        no=findViewById(R.id.cancelbutton);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        refreshAd();
    }
    private void refreshAd(){

        CardView cardView=findViewById(R.id.ad_container);
        AdMobHandler.getInstance(getOwnerActivity()).showNativeAd(cardView,progressBar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}