package com.rishabhmatharoo.blacklight.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.rishabhmatharoo.blacklight.Activity_Main;
import com.rishabhmatharoo.blacklight.CustomDialog.ShowRcValueDialog;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;


public class HomeScreen extends Fragment {

   FragmentActionListener fragmentActionListener;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    MediaPlayer player;
    boolean ismusicplaying=false;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState){
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

        final Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
       // loadIntertitialAd();


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
              //  loadIntertitialAd();
                     SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isSave,false);
                      if(fragmentActionListener!=null && !isStateSaved()){
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
    }
   /* private void loadIntertitialAd(){
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    */
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

}


