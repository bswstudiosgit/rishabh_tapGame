package com.rishabhmatharoo.blacklight.Fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.rishabhmatharoo.blacklight.Activity.Activity_Main;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Crashlytics.CrashlyticsTags;
import com.rishabhmatharoo.blacklight.CustomDialog.RewardAdPopupDialog;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Interfaces.GameViewInterface;
import com.rishabhmatharoo.blacklight.Interfaces.PopupCallBackFragmentInterface;
import com.rishabhmatharoo.blacklight.Interfaces.RewardAdCallBack;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.CustomDialog.SavePopupDialog;
import com.rishabhmatharoo.blacklight.RemoteConfig.RemoteColorModel;

import java.util.Random;
import java.util.Timer;

public class GameView extends Fragment implements PopupCallBackFragmentInterface, GameViewInterface {

    private ImageView orangecolor,bluecolor,yellowcolor,greencolor;
    private TextView scorecounter;
    public  int finalscore=0,prev_score=-1;
    private boolean gameover=false,hasAlreadytakenreward=false,rewardAdPopupActive=false,rewardAdFailedToLoad=false;
    private Timer timer;
    private ImageView backbutton;
    public  Handler handler=new Handler();
    public int finalscoreduringpausedgame=0;
    public int colornumber=0;
    private static String scoretag="Score";
    private static String colornumkey="colornum";
    public static String finalscorestring="FinalScore";
    private boolean isPausedapplication=false;
    private boolean isSavePopupActive=false;
    private boolean rewardAdLoaded=false;
    //Interfaces
    PopupCallBackFragmentInterface popupCallBackFragmentInterface;
    FragmentActionListener fragmentActionListener;
    GameViewInterface gameViewInterface;
    SavePopupDialog dialog;
    private RemoteColorModel remoteColorModel;
    private String handlerTimer="";
    private Handler refreshNativeAdHandler=new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,Bundle savedInstanceState) {
        gameViewInterface=this;
        rewardAdLoaded=false;
      remoteColorModel=new Gson().fromJson(SharedPreferenceClass.getInstance(getContext()).readColorValue(),
               RemoteColorModel.class);
        handlerTimer=SharedPreferenceClass.getInstance(getContext()).readHandlerTimer();
        Log.d("remoteHandlerTime",handlerTimer);
        //Load Reward Ad in onCreateView Method.
        AdMobHandler.getInstance(getActivity()).loadRewardAd();

        AdMobHandler.getInstance(getActivity()).setRewardAdCallBack(new RewardAdCallBack() {

            @Override
            public void onRewardAdEarned() {
                rewardAdPopupActive = false;
                hasAlreadytakenreward = true;
            }

            @Override
            public void onScreenChangeToGameOVer() {
                if(!hasAlreadytakenreward){
                    Bundle bundle = new Bundle();
                    if (fragmentActionListener != null ) {
                        bundle.putString(FragmentActionListener.FRAGMENT_NAME, "GameOver");
                        bundle.putInt("FinalScore", finalscore);
                        fragmentActionListener.onFragmentSelected(bundle);
                    }
                }
            }

            @Override
            public void onRewardAdLoaded() {
                rewardAdLoaded=true;
            }

            @Override
            public void onRewardAdFailedToLoad(){
                rewardAdFailedToLoad=true;
            }


        });

        addCutomKeyInCrashlytics();
        return inflater.inflate(R.layout.gameviewfragment,group,false);

    }
    @Override
    public void onViewCreated(View view,Bundle savedInstance){
        dialog=new SavePopupDialog(getActivity(),refreshNativeAdHandler);
        Log.d("GameView","Yes this is calling");
        orangecolor = (ImageView)view.findViewById(R.id.orangecolorid);
        bluecolor =(ImageView) view.findViewById(R.id.bluecolorid);
        yellowcolor=(ImageView) view.findViewById(R.id.yellowcolorid);
        greencolor=(ImageView) view.findViewById(R.id.greencolorid);
        scorecounter=(TextView) view.findViewById(R.id.scorecounter);
        //backgroundImg.setBackgroundColor(Color.parseColor("#dfe6e9"));
        backbutton=(ImageView)view.findViewById(R.id.backbutton);
        setallcolortonormal();
        if(SharedPreferenceClass.getInstance(getContext()).readboolean(SharedPreferenceClass.isSave)){

            finalscoreduringpausedgame=SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.savegamescore);
            colornumber=SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.colornum);

            //setallcolortonormal();
            //setaparticularcolorgrey(colornumber);
            //enablelistener(colornumber);
            colornumber++;
            if (colornumber >= 3) {
                colornumber = 0;
            }
        }

        SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.savegamescore,0);
        SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isSave,false);
        SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.colornum,0);

        if(savedInstance!=null) {
            Log.d("GameView","Calling save instances");
            finalscoreduringpausedgame = savedInstance.getInt(scoretag);

            colornumber=savedInstance.getInt(colornumkey);

         //       setallcolortonormal();

         //       setaparticularcolorgrey(colornumber);

           //     enablelistener(colornumber);
                colornumber++;
                if (colornumber >= 3) {
                    colornumber = 0;
                }

        }
        scorecounter.setText(getString(R.string.score) + finalscoreduringpausedgame);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isPopupActive,true);
                //Utilclass.isSavePopActive=true;
                if(!new RewardAdPopupDialog(getActivity()).isShowing()) {
                    isSavePopupActive = true;

                    SavePopupDialog dialog = new SavePopupDialog(getActivity(),refreshNativeAdHandler);
                    dialog.setCancelable(false);
                    dialog.setPopupCallBackFragmentInterface(popupCallBackFragmentInterface);
                    dialog.setGameViewInterFace(gameViewInterface);
                    SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.savegamescore, finalscore);
                    SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.colornum, colornumber);
                    //ExitDialog dialog1 = new ExitDialog(getActivity());
                    // dialog.setRungamehandler(GameView.this);
                    dialog.show();
                   // dialog1.show();
                    handler.removeCallbacksAndMessages(null);
                }

            }
        });


}


    public void rungame(int finalvar){
        Log.d("GameView",""+prev_score+" Final Score:"+finalscore);
        if(finalvar!=0){
            finalscore=finalvar;
        }else{
            finalscore=0;
            prev_score=-1;

        }
        final int[] prevrandomnumber = {0};
        timer = new Timer();
        //This Background thread will runs for every seconds.
        //setallcolortonormal();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    Random random = new Random();
                    colornumber = random.nextInt(4);
                    if (prevrandomnumber[0] == colornumber) {
                        colornumber++;
                        if (colornumber >= 3) {
                            colornumber = 0;
                        }
                    }
                    Log.d("GameView","PrevScore:"+prev_score+" Final Score:"+finalscore);
                    prevrandomnumber[0] = colornumber;
                    final int finalColornumber = colornumber;
                    final int finalColornumber1 = colornumber;
                    setallcolortonormal();
                    setaparticularcolorgrey(finalColornumber1);
                    enablelistener(finalColornumber);
                    if (prev_score == finalscore) {
                        GameOver();
                        gameover = true;
                    } else {
                        handler.postDelayed(this, Integer.parseInt(handlerTimer));
                    }
                    prev_score = finalscore;
                    scorecounter.setText(getString(R.string.score) + finalscore);

                }
        },Integer.parseInt(handlerTimer));


    }
    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt(scoretag,finalscore);
        outstate.putInt(colornumkey,colornumber);

        Log.d("GameView","OnSavedInstance");
    }



    @Override
    public void onPause() {
      handler.removeCallbacksAndMessages(null);
      refreshNativeAdHandler.removeCallbacksAndMessages(null);
        super.onPause();
        isPausedapplication=true;
        finalscoreduringpausedgame=finalscore;
        SharedPreferenceClass.getInstance(getContext()).write("score",finalscore);
        SharedPreferenceClass.getInstance(getContext()).writeBoolean("isapplicationinbg",true);

    }


    @Override
    public void onDestroyView() {
        Log.d("GameView","OnDestroyView");
        dialog.dismiss();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("GameView","OnDestroy");

        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
//        pausedapplication=true;

        if((!isPausedapplication || !isSavePopupActive) && !rewardAdPopupActive){
            Log.d("GameViewP","Onresume");
            prev_score=prev_score-1;
            colornumber++;
            if (colornumber >= 3) {
                colornumber = 0;
            }
            rungame(finalscoreduringpausedgame);
        }

        isPausedapplication=false;



    }

    @Override
    public void onStart() {
        Log.d("GameView","OnStart");

        super.onStart();
    }

    public void setallcolortonormal(){
        Drawable OrangeDrawable = getResources().getDrawable(R.drawable.orangecolorpanel);
        OrangeDrawable.setColorFilter(Color.parseColor(remoteColorModel.getColour1()), PorterDuff.Mode.SRC_ATOP);
        orangecolor.setBackground(OrangeDrawable);
        Drawable BlueDrawable=getResources().getDrawable(R.drawable.bluecolorpanel);
        BlueDrawable.setColorFilter(Color.parseColor(remoteColorModel.getColour2()),PorterDuff.Mode.SRC_ATOP);
        bluecolor.setBackground(BlueDrawable);
        Drawable YellowDrawable=getResources().getDrawable(R.drawable.yellowcolorpanel);
        YellowDrawable.setColorFilter(Color.parseColor(remoteColorModel.getColour3()),PorterDuff.Mode.SRC_ATOP);
        yellowcolor.setBackground(YellowDrawable);
        Drawable GreenDrawable=getResources().getDrawable(R.drawable.greycolorpanel);
        GreenDrawable.setColorFilter(Color.parseColor(remoteColorModel.getColour4()),PorterDuff.Mode.SRC_ATOP);
        greencolor.setBackground(GreenDrawable);

    }
    public void setaparticularcolorgrey(int id){

        Drawable GreyDrawable = getResources().getDrawable(R.drawable.greycolorpanel);
        GreyDrawable.setColorFilter(Color.parseColor(remoteColorModel.getColour5()), PorterDuff.Mode.SRC_ATOP);
        switch (id)
        {
            case 0:
                orangecolor.setBackground(GreyDrawable);
                //orangecolor.setBackgroundResource(R.drawable.greycolorpanel);
                //orangecolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;
            case 1:
                bluecolor.setBackground(GreyDrawable);
                //bluecolor.setBackgroundResource(R.drawable.greycolorpanel);
                //bluecolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;
            case 2:
                yellowcolor.setBackground(GreyDrawable);
                //yellowcolor.setBackgroundResource(R.drawable.greycolorpanel);
                //yellowcolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;
            case 3:
                greencolor.setBackground(GreyDrawable);
                //greencolor.setBackgroundResource(R.drawable.greycolorpanel);
                //greencolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;

        }
    }
    public void disableAllListener(){
        orangecolor.setEnabled(false);
        yellowcolor.setEnabled(false);
        greencolor.setEnabled(false);
        bluecolor.setEnabled(false);
    }
    public void enablelistener(int id){
        checkGameOver(id);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity_Main) {
            fragmentActionListener = ((FragmentActionListener) context);
            popupCallBackFragmentInterface=((PopupCallBackFragmentInterface)context);


        }


    }
    private void checkGameOver(final int buttonvalue){
        orangecolor.setEnabled(true);
        orangecolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orangecolor.setEnabled(false);
                if(buttonvalue!=0){
                    //orangecolor.setBackgroundResource(R.drawable.orangecolorpanel2);
                    GameOver();
                }else{
                    orangecolor.setBackgroundResource(R.drawable.greypanel2);
                    finalscore++;
                }
            }
        });
        bluecolor.setEnabled(true);
        bluecolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluecolor.setEnabled(false);
                if(buttonvalue!=1){
                    //bluecolor.setBackgroundResource(R.drawable.bluecolorpanel2);
                    GameOver();
                }else{
                    bluecolor.setBackgroundResource(R.drawable.greypanel2);
                    finalscore++;
                }
            }
        });
        yellowcolor.setEnabled(true);
        yellowcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yellowcolor.setEnabled(false);
                if(buttonvalue!=2){
                   // yellowcolor.setBackgroundResource(R.drawable.yellowcolorpanel2);
                    GameOver();
                }else{
                    yellowcolor.setBackgroundResource(R.drawable.greypanel2);
                    finalscore++;
                }
            }
        });
        greencolor.setEnabled(true);
        greencolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                greencolor.setEnabled(false);
                if(buttonvalue!=3){
                    GameOver();
                    //greencolor.setBackgroundResource(R.drawable.greencolorpanel2);
                }else{
                    greencolor.setBackgroundResource(R.drawable.greypanel2);
                    finalscore++;
                }
            }
        });
    }
    private void GameOver(){
        //Should not show popup when reward ad is failed to load.
        if(!hasAlreadytakenreward && !rewardAdFailedToLoad && rewardAdLoaded){

            disableAllListener();
            Log.d("rewardDialog","Dialog");
            RewardAdPopupDialog rewardAdPopupDialog=new RewardAdPopupDialog(getActivity());
            rewardAdPopupDialog.setCancelable(false);
            rewardAdPopupDialog.setRewardAdCallBack(new RewardAdCallBack() {
                @Override
                public void onRewardAdFailedToLoad() { }
                @Override
                public void onRewardAdEarned() { }
                @Override
                public void onScreenChangeToGameOVer() {
                    if(!hasAlreadytakenreward){
                        Bundle bundle = new Bundle();
                        if (fragmentActionListener != null ) {
                            bundle.putString(FragmentActionListener.FRAGMENT_NAME, "GameOver");
                            bundle.putInt("FinalScore", finalscore);
                            fragmentActionListener.onFragmentSelected(bundle); }
                    }
                }

                @Override
                public void onRewardAdLoaded() {

                }

            });
            rewardAdPopupDialog.show();
            rewardAdPopupActive=true;
        }else {
            Bundle bundle = new Bundle();
            if (fragmentActionListener != null && !isStateSaved()) {
                /*
                Bundle firebaseAnalyticsBundle=new Bundle();

                firebaseAnalyticsBundle.putString(FirebaseAnalytics.Param.SCORE,String.valueOf(finalscore));
                FirebaseAnalytics firebaseAnalytics=FirebaseAnalytics.getInstance(getContext());
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE,firebaseAnalyticsBundle);

                 */
                bundle.putString(FragmentActionListener.FRAGMENT_NAME, "GameOver");

                bundle.putInt(finalscorestring, finalscore);

                fragmentActionListener.onFragmentSelected(bundle);
            } else {
            }

            finalscore=0;
            finalscoreduringpausedgame=0;
        }
        handler.removeCallbacksAndMessages(null);


       /*
        RewardAdPopupDialog rewardAdPopupDialog=new RewardAdPopupDialog(getActivity());

        rewardAdPopupDialog.setCancelable(false);
        rewardAdPopupDialog.show();
        handler.removeCallbacksAndMessages(null);


        */
    }
/*
    public int stophandler(){
        handler.removeCallbacksAndMessages(null);
        Log.d("GameView","StopHandler");
        int score=finalscore;
        return score;

    }
*/

    @Override
    public void onCallBack(String str) {
        isSavePopupActive=false;
        getFragmentManager().popBackStack(str,0);

    }

    @Override
    public void stopHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void runHandler() {
        Log.d("Yes Calling","yes calling"+SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.savegamescore));
        rungame(SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.savegamescore));
        isSavePopupActive=false;
    }



    public void openpopup(){
        SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.savegamescore, finalscore);
        SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.colornum,colornumber);
       //SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isPopupActive,true);
        isSavePopupActive=true;
        //Utilclass.isSavePopActive=true;
        if(!new RewardAdPopupDialog(getActivity()).isShowing()) {

            dialog = new SavePopupDialog(getActivity(),refreshNativeAdHandler);
            dialog.setPopupCallBackFragmentInterface(popupCallBackFragmentInterface);
            dialog.setGameViewInterFace(gameViewInterface);

            dialog.setCancelable(false);
            dialog.show();
            AdMobHandler.getInstance(getActivity()).loadNativeAd();

            //ExitDialog dialog1=new ExitDialog(getActivity());
            //dialog1.show();
        }
    }




/*
    public void setFragmentActionListener3(FragmentActionListener fragmentActionListener1){
        this.fragmentActionListener=fragmentActionListener1;
    }
    public GameViewInterface getGameViewInterface1(){
        return gameViewInterface;
    }

*/

    private void addCutomKeyInCrashlytics(){
        FirebaseCrashlytics.getInstance().setCustomKey(CrashlyticsTags.ScreenTag,CrashlyticsTags.screen_name3);
        FirebaseCrashlytics.getInstance().setCustomKey(CrashlyticsTags.BestScoreTag,SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.BestScore));
        CrashlyticsTags.screenTransitions=CrashlyticsTags.screenTransitions+" > Game";
    }
}
