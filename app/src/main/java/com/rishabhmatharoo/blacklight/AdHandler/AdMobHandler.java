package com.rishabhmatharoo.blacklight.AdHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
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
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.rishabhmatharoo.blacklight.Activity_Main;
import com.rishabhmatharoo.blacklight.CustomDialog.ExitDialog;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Interfaces.TransactionCallBack;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.Util.Utilclass;

public class AdMobHandler {
    private static AdMobHandler instance;
    InterstitialAd mInterstitialAd;
    private Activity activity;
    private UnifiedNativeAd nativeAd;
    private RewardedAd rewardedAd;
    FragmentActionListener fragmentActionListener;
    private String interstitialAdUnitId="ca-app-pub-3940256099942544/1033173712";
    private String rewardAdUnitId="ca-app-pub-3940256099942544/5224354917";
    private String nativeAdUnitId="ca-app-pub-3940256099942544/2247696110";
    private String bannerAdId="ca-app-pub-3940256099942544/6300978111";
    public TransactionCallBack callBack;
    private AdMobHandler(Activity activity){

        this.activity=activity;
        MobileAds.initialize(activity.getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        if (activity instanceof Activity_Main) {
            fragmentActionListener = ((FragmentActionListener) activity);
        }

    }
    public synchronized static AdMobHandler getInstance(Activity activity){
        if(instance==null){
            instance=new AdMobHandler(activity);
        }
        return instance;
    }
    public void loadBannerAd(){
        AdView bannerad=new AdView(activity);
        bannerad.setAdUnitId(bannerAdId);
        bannerad.setAdSize(AdSize.SMART_BANNER);
        LinearLayout layout = (LinearLayout)activity.findViewById(R.id.adView);
        layout.addView(bannerad);

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerad.loadAd(adRequest);
        bannerad.setAdListener(new AdListener(){
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
        mInterstitialAd.setAdUnitId(interstitialAdUnitId);
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
    public void loadNativeAd(){

        AdLoader adLoader = new AdLoader.Builder(activity.getApplicationContext(), nativeAdUnitId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        //if(nativeAd!=null)
                            nativeAd=unifiedNativeAd;
                        /*
                        populateNativeAd(adView,unifiedNativeAd);
                         */
                        //UnifiedNativeAdView adView=(UnifiedNativeAdView)activity.getLayoutInflater().inflate(R.layout.native_ad_layout,null);

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
    public void showNativeAd(CardView cardView){
        UnifiedNativeAdView adView=(UnifiedNativeAdView)activity.getLayoutInflater().inflate(R.layout.native_ad_layout,null);
        populateNativeAd(adView,nativeAd);
        cardView.removeAllViews();
        cardView.addView(adView);
       // progressBar.setVisibility(View.INVISIBLE);

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
        loadNativeAd();
    }
    public void destroyNativeAds(){
        if(nativeAd!=null){
            nativeAd.destroy();


        }
    }
    public void loadRewardAd(){
        rewardedAd = new RewardedAd(activity,
                rewardAdUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                Log.d("RewardAd","Ad is loaded Completed");

            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Log.d("RewardAd","Ad load failed");
                //This boolean will not let Reward Ad popup to be displayed.
                Utilclass.hasAlreadytakenreward=true;

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }
    public void showRewardAd(){
        if (rewardedAd.isLoaded()) {

            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    if(!Utilclass.hasAlreadytakenreward){

                       /* Bundle bundle = new Bundle();
                        if (fragmentActionListener != null ) {
                            bundle.putString(FragmentActionListener.FRAGMENT_NAME, "GameOver");
                            bundle.putInt("FinalScore", Utilclass.finalScoreDuringRewardAd);
                            fragmentActionListener.onFragmentSelected(bundle);
                        }

                        */
                       callBack.onScreentransaction();
                    }

                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    Log.d("Reward",""+reward.getAmount());
                    Utilclass.hasAlreadytakenreward=true;
                    Utilclass.rewardAdPopupActive=false;
                        Log.d("ExitGameDialog","Dismiss");
                        //new ExitDialog(activity).dismiss();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show(activity, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }

    }
    public void setCallBackReference(TransactionCallBack transactionCallBack){
        this.callBack=transactionCallBack;
    }

}
