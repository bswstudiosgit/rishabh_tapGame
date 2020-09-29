package com.rishabhmatharoo.blacklight.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Crashlytics.CrashlyticsTags;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;

public class SplashScreenFragment  extends Fragment {
    FragmentActionListener fragmentActionListener;
    // boolean alreadyloaded=false;

    public void setFragmentActionListener4(FragmentActionListener fragmentActionListener1){
        this.fragmentActionListener=fragmentActionListener1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        //getSharedPreferenceBestScore();
        AdMobHandler.getInstance(getActivity()).pauseBannerAd();
        addCutomKeyInCrashlytics();
        return inflater.inflate(R.layout.splashscreen,parent,false);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        /*if(!alreadyloaded){*/
            ImageView title=(ImageView)view.findViewById(R.id.tapview);
            final Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.bounce3);
           // alreadyloaded=true;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(SharedPreferenceClass.getInstance(getContext()).readboolean(SharedPreferenceClass.isSave)){
                        if(fragmentActionListener!=null && !isStateSaved()){
                            Bundle bundle=new Bundle();
                            bundle.putString(FragmentActionListener.FRAGMENT_NAME,"GameView");
                            FragmentManager fragmentManager=getFragmentManager();

                            Log.d("SplashScreen","Yes calling");
                            fragmentActionListener.onFragmentSelected(bundle);
                        }
                    }else{
                        if(fragmentActionListener!=null && !isStateSaved()){
                            Bundle bundle=new Bundle();
                            bundle.putString(FragmentActionListener.FRAGMENT_NAME,"HomeScreen");
                            fragmentActionListener.onFragmentSelected(bundle);
                        }
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            title.startAnimation(animation);

        /*
        }else{
            PopupManagerClass ft=getFragmentManager();
            ft.popBackStack();
        }

         */

    }
    private void addCutomKeyInCrashlytics(){
        FirebaseCrashlytics.getInstance().setCustomKey(CrashlyticsTags.ScreenTag,CrashlyticsTags.screen_name1);
        FirebaseCrashlytics.getInstance().setCustomKey(CrashlyticsTags.BestScoreTag,SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.BestScore));
        CrashlyticsTags.screenTransitions="Splash";
    }
    private void getSharedPreferenceBestScore(){
        try {
            if(SharedPreferenceClass.getInstance(getContext()).readnullvalue(SharedPreferenceClass.wrongkeyofbestscore).isEmpty()){

                //FirebaseCrashlytics.getInstance().log("Shared Preference Best Score Not Found");
                throw new RuntimeException("Null Pointer Exception");
            }
        }catch (Exception e){
            FirebaseCrashlytics.getInstance().setCustomKey("logs","Shared Preference Best Score Not Found");
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
