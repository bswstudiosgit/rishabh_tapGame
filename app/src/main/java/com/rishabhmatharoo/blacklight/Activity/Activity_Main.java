package com.rishabhmatharoo.blacklight.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Crashlytics.CrashlyticsTags;
import com.rishabhmatharoo.blacklight.Fragments.GameOverFragment;
import com.rishabhmatharoo.blacklight.Fragments.GameView;
import com.rishabhmatharoo.blacklight.Fragments.HomeScreen;
import com.rishabhmatharoo.blacklight.Fragments.LanguageFragment;
import com.rishabhmatharoo.blacklight.Fragments.SplashScreenFragment;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Interfaces.GameViewInterface;
import com.rishabhmatharoo.blacklight.Interfaces.PopupCallBackFragmentInterface;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.RemoteConfig.RemoteConfigKey;

import java.util.Locale;

public class Activity_Main extends AppCompatActivity implements FragmentActionListener, PopupCallBackFragmentInterface {
    String Fragmenttag="";
    private String homescreenstr="HomeScreen";
    private String homescreentag="homescreen";
    private String gameviewstr="GameView";
    private String gameviewtag="gameview";
    private String gameoverstr="GameOver";
    private String gameovertag="gameover";
    private String languagestr="LanguageFragment";
    private String languagetag="languagefragment";
    String TAG="$$$";
    FirebaseRemoteConfig firebaseRemoteConfig= FirebaseRemoteConfig.getInstance();
    AdView adView;
    GameViewInterface gameViewInterface;
    int cachetime=3600;
    private String bannerAdId="ca-app-pub-3940256099942544/6300978111";
    private FrameLayout adContainerView;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadRcValues(0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activitymain);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
       /* adView=findViewById(R.id.adView);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        */
        AdMobHandler.getInstance(Activity_Main.this).loadIntertitialAd();
        AdMobHandler.getInstance(Activity_Main.this).loadNativeAd();

        View overlay = findViewById(R.id.popupview);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //fullScreenCall();
        checkAndChangelanguage(SharedPreferenceClass.getInstance(this).read(SharedPreferenceClass.language));

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SplashScreenFragment splashScreenFragment = new SplashScreenFragment();
            splashScreenFragment.setFragmentActionListener4(this);
            ft.replace(R.id.yourfragment, splashScreenFragment);
          //  ft.addToBackStack("splashscreen");
            ft.commit();
        }

        adContainerView = findViewById(R.id.adView);

        AdMobHandler.getInstance(this).loadBannerAd(adContainerView);
        AdMobHandler.getInstance(this).loadInterstitialVideoAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AdMobHandler.getInstance(this).resumeBannerAd();
        fullScreenCall();
        AdMobHandler.getInstance(this).resumeBannerAd();


    }
    private void saveInSharedPrefrence(){
        SharedPreferenceClass.getInstance(this).writeColorValues(firebaseRemoteConfig.getString(RemoteConfigKey.ColorValue));
        String Handlertime=firebaseRemoteConfig.getString(RemoteConfigKey.handlertimer);
        SharedPreferenceClass.getInstance(this).writeHandlerTimer(Handlertime);
        String value=firebaseRemoteConfig.getString("BG_Var1");
        SharedPreferenceClass.getInstance(this).writeBgMusic(value);
        SharedPreferenceClass.getInstance(this).writeInterstitialFrequency(firebaseRemoteConfig.getString(RemoteConfigKey.Interstitialkey));
        Log.d("RemoteHandlerTime",Handlertime);
        Log.d("Bgvar",firebaseRemoteConfig.getString("BG_Var1"));

    }
    @Override
    public void onFragmentSelected(Bundle bundle) {

        String Fragmentname=bundle.getString(FragmentActionListener.FRAGMENT_NAME);
        Fragmenttag=Fragmentname;
        if(Fragmentname.equalsIgnoreCase(homescreenstr)){

            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.yourfragment,new HomeScreen(),homescreentag);

            ft.commit();

        }else if(Fragmentname.equalsIgnoreCase(gameviewstr)){

            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(gameviewtag);
            ft.replace(R.id.yourfragment,new GameView(),gameviewtag);
            ft.commit();
        }else if(Fragmentname.equalsIgnoreCase(gameoverstr) ){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            GameOverFragment gameOverFragment=new GameOverFragment();
            gameOverFragment.setFragmentActionListener2(this);
            gameOverFragment.setArguments(bundle);
            ft.replace(R.id.yourfragment,gameOverFragment,gameovertag);
            ft.addToBackStack(gameovertag);
            ft.commit();

        }else if(Fragmentname.equalsIgnoreCase(languagestr) ){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            LanguageFragment languageFragment=new LanguageFragment();
            languageFragment.setFragmentActionListener5(this);
            languageFragment.setArguments(bundle);
            ft.replace(R.id.yourfragment,languageFragment,languagetag);
            ft.commit();

        }


    }
    @Override
    public void onBackPressed() {
        final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.yourfragment);
        if(fragmentInFrame instanceof HomeScreen){
            CrashlyticsTags.screenTransitions=CrashlyticsTags.screenTransitions+" > Exit";
            this.finish();
            //ExitDialog dialog=new ExitDialog(this);
            //dialog.show();
        }else if(fragmentInFrame instanceof GameView){
            //gameViewInterface=((GameView) fragmentInFrame).getGameViewInterface1();
            //gameViewInterface.stopHandler();
            //gameViewInterface.openpopup();
            ((GameView) fragmentInFrame).stopHandler();
            ((GameView)fragmentInFrame).openpopup();
        }else if(fragmentInFrame instanceof GameOverFragment){
            popFragment(gameviewtag);
            homescreentransaction();
        }else if(fragmentInFrame instanceof LanguageFragment){
            homescreentransaction();
        }
    }

    public void popFragment(String popstr){
        getSupportFragmentManager().popBackStack(popstr,FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public void homescreentransaction() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.yourfragment, new HomeScreen());
        ft.commit();
    }

    @Override
    public void onCallBack(String str) {
            getSupportFragmentManager().popBackStack(str, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            homescreentransaction();
      }



    public void checkAndChangelanguage(int id){
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
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }
    public void fullScreenCall() {

        //This code is for when dialog box show then nav bar appear and then it doesn't go back
        //so below is the code check the changes in Navbar and then remove the nav bar.
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ) == 0) {
                            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                            decorView.setSystemUiVisibility(uiOptions);

                        }
                    }
                });
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }*/
    }



    private void logInstallationAuthToken() {
        // [START get_installation_token]
        FirebaseInstallations.getInstance().getToken(/* forceRefresh */false)
                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Log.d("Installations", "Installation auth token: " + task.getResult().getToken());
                        } else {
                            Log.e("Installations", "Unable to get Installation auth token");
                        }
                    }
                });
        // [END get_installation_token]
    }

    private void logInstallationID() {
        // [START get_installation_id]
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            Log.d("Installations", "Installation ID: " + task.getResult());
                        } else {
                            Log.e("Installations", "Unable to get Installation ID");
                        }
                    }
                });
        // [END get_installation_id]
    }

    private void deleteInstallation() {
        // [START delete_installation]
        FirebaseInstallations.getInstance().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Installations", "Installation deleted");
                        } else {
                            Log.e("Installations", "Unable to delete Installation");
                        }
                    }
                });
        // [END delete_installation]
    }

    private void loadRcValues(int time){
        //set Throlling every time value seconds.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(time)
                .build();
       logInstallationAuthToken();
       // logInstallationID();
        //deleteInstallation();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
                    Log.d(TAG, "Config params updated: " + updated);
                    //Toast.makeText(Activity_Main.this, "Fetch and activate succeeded",
                      //      Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(Activity_Main.this, "Fetch failed",
                      //      Toast.LENGTH_SHORT).show();
                }
                saveInSharedPrefrence();
            }

        });

    }

    @Override
    protected void onPause() {

        super.onPause();
        AdMobHandler.getInstance(this).pauseBannerAd();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        AdMobHandler.getInstance(this).destroyNativeAds();
        //AdMobHandler.getInstance(this).destroyBannerAd();
        //adView.destroy();
        AdMobHandler.getInstance(this).destroyBannerAd();


    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }


    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(this, adWidth);
    }
}
