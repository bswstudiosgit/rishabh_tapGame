package com.rishabhmatharoo.blacklight.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rishabhmatharoo.blacklight.Activity.Activity_Main;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;

public class GameOverFragment extends Fragment {
    int finalscore=0;
    FragmentActionListener fragmentActionListener;
    InterstitialAd mInterstitialAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
       if(this.getArguments()!=null){
           finalscore=this.getArguments().getInt("FinalScore");
       }
       SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.playerExperience,
               SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.playerExperience)+1);
       UpdatePlayerExperienceInGoogleAnalytics(SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.playerExperience));
        AdMobHandler.getInstance(getActivity()).showIntertitialAd();
       if(!AdMobHandler.getInstance(getActivity()).isInterstitialAdLoaded())
           AdMobHandler.getInstance(getActivity()).loadIntertitialAd();
        return inflater.inflate(R.layout.gameoverscreen,parent,false);
    }
    public void setFragmentActionListener2(FragmentActionListener fragmentActionListener1){
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
    public void onViewCreated(View view,Bundle savedInstanceState){
        //Setup any Handles to view objects here
        final Button replay=(Button) view.findViewById(R.id.replaybutton);

        TextView currentscore=(TextView)view.findViewById(R.id.currentscore);
        final int score=finalscore;
        Bundle firebaseAnalyticsBundle=new Bundle();
        firebaseAnalyticsBundle.putString(FirebaseAnalytics.Param.SCORE,String.valueOf(finalscore));
        FirebaseAnalytics.getInstance(getContext()).logEvent(FirebaseAnalytics.Event.POST_SCORE,firebaseAnalyticsBundle);
        currentscore.setText(getString(R.string.currentscore)+" "+score);
        TextView bestscore=(TextView)view.findViewById(R.id.bestscore);
        //if current score is greater then best score then replace with current score.
        if(SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.BestScore)<finalscore){

            SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.BestScore,finalscore);
                bestscore.setText(getString(R.string.bestscore)+" "+finalscore);
        }else{
                bestscore.setText(getString(R.string.bestscore)+" "+SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.BestScore));
        }
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                         if(fragmentActionListener!=null){
                            Bundle bundle=new Bundle();
                            bundle.putString(FragmentActionListener.FRAGMENT_NAME,"GameView");
                            fragmentActionListener.onFragmentSelected(bundle);
                        }else {
                            Log.d("Button Played","yes");
                        }
                   // PopupManagerClass fragmentManager=getFragmentManager();
                    //fragmentManager.popBackStack("gameview",0);
            }
        });
        final Button quit=(Button)view.findViewById(R.id.exitbutton);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*
                        Log.d("Button Played","yes");
                        if(fragmentActionListener!=null){
                            Bundle bundle=new Bundle();
                            bundle.putString(FragmentActionListener.FRAGMENT_NAME,"HomeScreen");
                            fragmentActionListener.onFragmentSelected(bundle);
                        }else {
                            Log.d("Button Played","yes");
                        }
                    */

                    if(!isStateSaved() && fragmentActionListener!=null){

                        FragmentManager fragmentManager=getFragmentManager();
                       if(fragmentManager.popBackStackImmediate("homescreen",0)){
                           fragmentManager.popBackStack("homescreen",0);
                       }else{
                           Bundle bundle=new Bundle();
                           bundle.putString(FragmentActionListener.FRAGMENT_NAME,"HomeScreen");
                           fragmentActionListener.onFragmentSelected(bundle);
                           //fragmentManager.popBackStack();

                       }
                    }
            }
        });
    }
    private void UpdatePlayerExperienceInGoogleAnalytics(int experience){
        String userProperty="experience";
        String userExperience="";
        if(experience<4){
            userExperience="Beginner";
        }else if(experience<15 && experience>4){
            userExperience="Intermediate";
            Log.d("UserExperience","Intermediate");
        }else if(experience>=15){
            userExperience="Expert";
            Log.d("UserExperience","Expert");
        }
        FirebaseAnalytics.getInstance(getContext()).setUserProperty(userProperty,userExperience);
    }

}
