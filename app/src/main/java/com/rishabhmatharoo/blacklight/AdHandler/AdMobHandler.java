package com.rishabhmatharoo.blacklight.AdHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rishabhmatharoo.blacklight.Activity_Main;
import com.rishabhmatharoo.blacklight.R;

public class AdMobHandler {
    private static AdMobHandler instance;
    InterstitialAd mInterstitialAd;
    ProgressDialog progressDialog;

    private Activity activity;
    private UnifiedNativeAd nativeAd;

    private AdMobHandler(Activity activity){

        this.activity=activity;
        MobileAds.initialize(activity.getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

    }
    public synchronized static AdMobHandler getInstance(Activity activity){
        if(instance==null){
            instance=new AdMobHandler(activity);
        }
        return instance;
    }
    public void loadBannerAd(AdView adView){
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("BannerAd",adError.toString());
            }
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("BannerAd","Finish");
            }

        });
    }
    public void loadIntertitialAd(){
        mInterstitialAd = new InterstitialAd(activity.getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.d("InterstialAd",adError.toString());
                }
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Log.d("InterstialAd","Finish");
                }
                @Override
                 public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
    public void showIntertitialAd(){
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadAgainInterstitial();
        }

    }
    public void loadAgainInterstitial(){
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }
    public void showNativeAd(final CardView cardView,final ProgressBar progressBar){

        AdLoader adLoader = new AdLoader.Builder(activity.getApplicationContext(), "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        if(nativeAd!=null)
                            nativeAd=unifiedNativeAd;
                        UnifiedNativeAdView adView=(UnifiedNativeAdView)activity.getLayoutInflater().inflate(R.layout.native_ad_layout,null);
                        populateNativeAd(adView,unifiedNativeAd);
                        cardView.removeAllViews();
                        cardView.addView(adView);
                        //refresh.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
    private void populateNativeAd(UnifiedNativeAdView nativeAdView, UnifiedNativeAd nativeAd){
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.adv_headline));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.adv_bodytext));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.star_rating));
        nativeAdView.setMediaView((MediaView)nativeAdView.findViewById(R.id.ad_mediaview));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.adv_icon));
        if(nativeAd.getMediaContent()==null){
            nativeAdView.getMediaView().setVisibility(View.INVISIBLE);
        }else{
            nativeAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());
            nativeAdView.getMediaView().setVisibility(View.VISIBLE);
        }
        ((TextView)nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        if(nativeAd.getBody()==null){
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        }else{
            ((TextView)nativeAdView.getBodyView()).setText(nativeAd.getBody());
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
        }
        if(nativeAd.getAdvertiser()==null){
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        }else{
            ((TextView)nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        if(nativeAd.getStarRating()==null){
            nativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        }else{
            ((RatingBar)nativeAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);

        }
        if(nativeAd.getIcon()==null){
            nativeAdView.getIconView().setVisibility(View.INVISIBLE);
        }else{
            ((ImageView)nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if(nativeAd.getCallToAction()==null){
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        }else{
            ((Button)nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
        }
        nativeAdView.setNativeAd(nativeAd);
    }
    public void destroyNativeAds(){
        if(nativeAd!=null){
            nativeAd.destroy();

        }

    }
}
