package com.rishabhmatharoo.blacklight.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rishabhmatharoo.blacklight.Activity.Activity_Main;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Crashlytics.CrashlyticsTags;
import com.rishabhmatharoo.blacklight.CustomDialog.ShowRcValueDialog;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;


public class HomeScreen extends Fragment {

   FragmentActionListener fragmentActionListener;


    MediaPlayer player;
    boolean ismusicplaying=false;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
        AdMobHandler.getInstance(getActivity()).resumeBannerAd();
        addCutomKeyInCrashlytics();
        return inflater.inflate(R.layout.fragment_home_screen,parent,false);

    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener1){
        this.fragmentActionListener=fragmentActionListener1;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity_Main) {
            fragmentActionListener = ((FragmentActionListener) context);
        }


    }



    @Override
    public void onResume() {
        super.onResume();
        if(!ismusicplaying) {
            loadBackgroundMusic();
            playandstop(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playandstop(1);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        //Setup any Handles to view objects here
        final Button play=(Button) view.findViewById(R.id.playbutton);
        final Button continuebtn=(Button)view.findViewById(R.id.countinuebutton);
        final Button language=(Button)view.findViewById(R.id.languagebutton);
        final Button rcvalues=(Button)view.findViewById(R.id.rcvalues);
        final Button crashbutton=(Button)view.findViewById(R.id.crashButton);
        final Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
        final Button SubscribeButton=(Button)view.findViewById(R.id.SubscribeButton);


/*        if(SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.language)==2) {
            //Conditon for arabic language
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

        }/*else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

        }*/
        play.startAnimation(animation);
        if(SharedPreferenceClass.getInstance(getContext()).readboolean(SharedPreferenceClass.isSave)){
            continuebtn.setVisibility(View.VISIBLE);
        }else{
            continuebtn.setVisibility(View.GONE);
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("HomeScreenListener","yes working");

                     SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isSave,false);
                      if(fragmentActionListener!=null && !isStateSaved()){
                          Bundle firebaseanalyticsBundle=new Bundle();
                          FirebaseAnalytics.getInstance(getContext()).logEvent("game_start",firebaseanalyticsBundle);
                          int num=Integer.parseInt(SharedPreferenceClass.getInstance(getContext()).readInterstitialFrequency().isEmpty()?"2":
                                  SharedPreferenceClass.getInstance(getContext()).readInterstitialFrequency());
                          if(AdMobHandler.getInstance(getActivity()).hasInterstitialAdForGameView(num)){
                              //AdMobHandler.getInstance(getActivity()).showIntertitialAd();

                              AdMobHandler.getInstance(getActivity()).showInterstitialVideoAd();

                              AdMobHandler.getInstance(getActivity()).decrementNumberOfInterstitialLoad();
                              AdMobHandler.getInstance(getActivity()).loadInterstitialVideoAd();

                          }

                          Bundle bundle=new Bundle();
                          bundle.putString(FragmentActionListener.FRAGMENT_NAME,"GameView");
                          fragmentActionListener.onFragmentSelected(bundle);


                      }


            }
        });
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentActionListener!=null && !isStateSaved()){
                    Bundle bundle=new Bundle();
                    bundle.putString(FragmentActionListener.FRAGMENT_NAME,"GameView");

                    fragmentActionListener.onFragmentSelected(bundle);
                }
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fragmentActionListener!=null && !isStateSaved()){
                    Bundle bundle=new Bundle();
                    bundle.putString(FragmentActionListener.FRAGMENT_NAME,"LanguageFragment");

                    fragmentActionListener.onFragmentSelected(bundle);
                }


            }
        });
        rcvalues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRcValueDialog showRcValueDialog=new ShowRcValueDialog(getActivity());
                showRcValueDialog.show();
            }
        });
        crashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Operations on FirebaseCrashlytics.
                FirebaseCrashlytics.getInstance().log(CrashlyticsTags.screenTransitions);
                throw new RuntimeException("Test Crash");

            }
        });
        SubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager tm = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
                String countryCodeValue = tm.getNetworkCountryIso();

                Log.d("CountryCode:",countryCodeValue);
                FirebaseMessaging.getInstance().subscribeToTopic(countryCodeValue);
            }
        });

    }



    private void loadBackgroundMusic(){
        if(SharedPreferenceClass.getInstance(getActivity()).readBgMusic().equals("0")){
           player=MediaPlayer.create(getActivity(),R.raw.anothermusic);
           player.setLooping(true);

        }else{
           player=MediaPlayer.create(getActivity(),R.raw.guitarsolo);
            player.setLooping(true);
        }
    }
    private void playandstop(int action){
        if(action==0){
            player.start();
            ismusicplaying=true;
        }else{
            ismusicplaying=false;
            player.stop();
        }
    }
    private void addCutomKeyInCrashlytics(){
        FirebaseCrashlytics.getInstance().setCustomKey(CrashlyticsTags.ScreenTag,CrashlyticsTags.screen_name2);
        FirebaseCrashlytics.getInstance().setCustomKey(CrashlyticsTags.BestScoreTag,SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.BestScore));
        CrashlyticsTags.screenTransitions=CrashlyticsTags.screenTransitions+" > Home";
    }

}


