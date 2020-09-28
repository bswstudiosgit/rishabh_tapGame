package com.rishabhmatharoo.blacklight.AdHandler;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Interfaces.RewardAdCallBack;
import com.rishabhmatharoo.blacklight.Interfaces.TransactionCallBack;
import com.rishabhmatharoo.blacklight.R;

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
    private RewardAdCallBack rewardAdCallback;
    private int numberOfInterstialLoad=0;
    private FrameLayout bannerLayout;

    AdView bannerad;
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
    public void loadBannerAd(final FrameLayout layout){
        this.bannerLayout=layout;
          layout.post(new Runnable() {
            @Override
            public void run() {
                bannerad = new AdView(activity);
                bannerad.setAdUnitId(bannerAdId);

                layout.removeAllViews();
                layout.addView(bannerad);
                AdSize adSize = getAdSize();
                bannerad.setAdSize(adSize);
                AdRequest adRequest = new AdRequest.Builder().build();
                bannerad.loadAd(adRequest);
                bannerad.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        Toast.makeText(activity.getApplicationContext(), "Banner: Ad is Loaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                        Toast.makeText(activity.getApplicationContext(), "Banner: Ad is failed to load error" + adError.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                        Toast.makeText(activity.getApplicationContext(), "Banner: Ad is Opened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                        Toast.makeText(activity.getApplicationContext(), "Banner: Ad is Clicked", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
    public void resumeBannerAd(){
        if(bannerad!=null) {
            Log.d("BannerAd","onresume");
            bannerad.resume();
            bannerLayout.setVisibility(View.VISIBLE);
        }
    }
    public void pauseBannerAd(){
        if(bannerad!=null) {
            Log.d("BannerAd","onPause");
            bannerad.pause();
            bannerLayout.setVisibility(View.INVISIBLE);
        }
    }
    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
    public void loadIntertitialAd(){
        mInterstitialAd = new InterstitialAd(activity.getApplicationContext());
        mInterstitialAd.setAdUnitId(interstitialAdUnitId);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Toast.makeText(activity.getApplicationContext(),"Interstitial: Ad is Failed to load"+adError.toString(),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Toast.makeText(activity.getApplicationContext(),"Interstitial: Ad is Loaded",Toast.LENGTH_SHORT).show();
                    Log.d("InterstialAd","Finish");
                    numberOfInterstialLoad++;
                }
                @Override
                 public void onAdClosed() {
                // Load the next interstitial.

                    Toast.makeText(activity.getApplicationContext(),"Interstitial: Ad is Closed",Toast.LENGTH_SHORT).show();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Toast.makeText(activity.getApplicationContext(),"Interstitial: Ad is Clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isInterstitialAdLoaded(){
        return mInterstitialAd.isLoaded();
    }
    public boolean hasInterstitialAdForGameView(){
        if(numberOfInterstialLoad>=2){
            numberOfInterstialLoad=0;
            return true;
        }
        return false;
    }
    public void decrementNumberOfInterstitialLoad(){
        numberOfInterstialLoad--;
    }
    public void showIntertitialAd(){
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }
    public void loadAgainInterstitial(){
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.d("InterstialAd",adError.toString());
                    showIntertitialAd();
                }
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                   // Log.d("InterstialAd","Finish");
                }
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            });
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
                        //loadNativeAd();

                        Toast.makeText(activity.getApplicationContext(),"NativeAd Failed"+adError.toString(),Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        Toast.makeText(activity.getApplicationContext(),"NativeAd Loaded",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onAdClosed() {
                        Toast.makeText(activity.getApplicationContext(),"NativeAd Closed",Toast.LENGTH_SHORT).show();
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
        //If ad is not loaded then nativeAd will has null value.
        if(nativeAd!=null ){
            UnifiedNativeAdView adView=(UnifiedNativeAdView)activity.getLayoutInflater().inflate(R.layout.native_ad_layout,null);
            populateNativeAd(adView,nativeAd);
            cardView.removeAllViews();
            cardView.addView(adView);
        }else{
            cardView.setVisibility(View.GONE);
        }

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
    public void destroyBannerAd(){
        if(bannerad!=null) {
            bannerad.destroy();
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
                Toast.makeText(activity.getApplicationContext(),"RewardAdLoaded",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                Log.d("RewardAd","Ad load failed");
                rewardAdCallback.onRewardAdFailedToLoad();
                Toast.makeText(activity.getApplicationContext(),"RewardAdFailedToLoad",Toast.LENGTH_SHORT).show();
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
                       callBack.onScreentransaction();
                }
                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                   rewardAdCallback.onRewardAdEarned();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show(activity, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            loadRewardAd();
        }

    }
    public void setCallBackReference(TransactionCallBack transactionCallBack){
        this.callBack=transactionCallBack;
    }
    public void setRewardAdCallBack(RewardAdCallBack rewardAdCallBack){
        this.rewardAdCallback=rewardAdCallBack;
    }

}
