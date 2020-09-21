package com.rishabhmatharoo.blacklight.Fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rishabhmatharoo.blacklight.Activity_Main;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Interfaces.GameViewInterface;
import com.rishabhmatharoo.blacklight.Interfaces.PopupCallBackFragmentInterface;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.CustomDialog.SavePopupDialog;
import com.rishabhmatharoo.blacklight.RemoteConfig.RemoteColorModel;
import com.rishabhmatharoo.blacklight.RemoteConfig.RemoteConfigKey;
import com.rishabhmatharoo.blacklight.Util.Utilclass;

import java.util.Random;
import java.util.Timer;

public class GameView extends Fragment implements PopupCallBackFragmentInterface, GameViewInterface {

    private ImageView orangecolor,bluecolor,yellowcolor,greencolor;
    private TextView scorecounter;
    public  int finalscore=0,prev_score=-1;
    private boolean gameover=false;
    private Timer timer;
    private ImageView backbutton;
    public  Handler handler=new Handler();
    public int finalscoreduringpausedgame=0;
    public int colornumber=0;
    private static String scoretag="Score";
    private static String colornumkey="colornum";
    public static String finalscorestring="FinalScore";
    private boolean isPausedapplication=false;
    //Interfaces
    PopupCallBackFragmentInterface popupCallBackFragmentInterface;
    FragmentActionListener fragmentActionListener;
    GameViewInterface gameViewInterface;
    SavePopupDialog dialog;
    private RemoteColorModel remoteColorModel;
    private String handlerTimer="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group,Bundle savedInstanceState) {
        gameViewInterface=this;
      remoteColorModel=new Gson().fromJson(SharedPreferenceClass.getInstance(getContext()).readColorValue(),
               RemoteColorModel.class);
        handlerTimer=SharedPreferenceClass.getInstance(getContext()).readHandlerTimer();

        Log.d("remoteHandlerTime",handlerTimer);
        return inflater.inflate(R.layout.gameviewfragment,group,false);

    }
    @Override
    public void onViewCreated(View view,Bundle savedInstance){
        dialog=new SavePopupDialog(getActivity());
        Log.d("GameView","Yes this is calling");
        orangecolor = (ImageView)view.findViewById(R.id.orangecolorid);
        bluecolor =(ImageView) view.findViewById(R.id.bluecolorid);
        yellowcolor=(ImageView) view.findViewById(R.id.yellowcolorid);
        greencolor=(ImageView) view.findViewById(R.id.greencolorid);
        scorecounter=(TextView) view.findViewById(R.id.scorecounter);
        //backgroundImg.setBackgroundColor(Color.parseColor("#dfe6e9"));
        backbutton=(ImageView)view.findViewById(R.id.backbutton);
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
                Utilclass.isSavePopActive=true;
                SavePopupDialog dialog=new SavePopupDialog(getActivity());
                dialog.setCancelable(false);
                dialog.setPopupCallBackFragmentInterface(popupCallBackFragmentInterface);
                dialog.setGameViewInterFace(gameViewInterface);
                SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.savegamescore, finalscore);
                SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.colornum,colornumber);

              //  dialog.setRungamehandler(GameView.this);
                dialog.show();
                handler.removeCallbacksAndMessages(null);

            }
        });

}


    public void rungame(int finalvar){
        if(finalvar!=0){
            finalscore=finalvar;
        }else{
            finalscore=0;
            prev_score=-1;

        }
        final int[] prevrandomnumber = {0};
        timer = new Timer();
        //This Background thread will runs for every seconds.
        setallcolortonormal();
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
        Log.d("GameView","Onresume");
        super.onResume();
//        pausedapplication=true;

        if(!isPausedapplication || !Utilclass.isSavePopActive){
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
        orangecolor.setBackgroundResource(R.drawable.orangecolorpanel);
        orangecolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour1()));

        bluecolor.setBackgroundResource(R.drawable.bluecolorpanel);
        bluecolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour2()));
        yellowcolor.setBackgroundResource(R.drawable.yellowcolorpanel);
        yellowcolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour3()));
        greencolor.setBackgroundResource(R.drawable.greenpanel);
        greencolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour4()));

    }
    public void setaparticularcolorgrey(int id){
        switch (id)
        {
            case 0:
                orangecolor.setBackgroundResource(R.drawable.greycolorpanel);
                orangecolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;
            case 1:
                bluecolor.setBackgroundResource(R.drawable.greycolorpanel);
                bluecolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;
            case 2:
                yellowcolor.setBackgroundResource(R.drawable.greycolorpanel);
                yellowcolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;
            case 3:
                greencolor.setBackgroundResource(R.drawable.greycolorpanel);
                greencolor.setBackgroundColor(Color.parseColor(remoteColorModel.getColour5()));
                break;

        }
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
        Bundle bundle=new Bundle();
        if(fragmentActionListener!=null && !isStateSaved()){

            bundle.putString(FragmentActionListener.FRAGMENT_NAME,"GameOver");

            bundle.putInt(finalscorestring,finalscore);

            fragmentActionListener.onFragmentSelected(bundle);
        }else {
        }
        finalscore=0;
        finalscoreduringpausedgame=0;
        handler.removeCallbacksAndMessages(null);

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
    }

    public void openpopup(){

        SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.savegamescore, finalscore);
        SharedPreferenceClass.getInstance(getContext()).write(SharedPreferenceClass.colornum,colornumber);
       //SharedPreferenceClass.getInstance(getContext()).writeBoolean(SharedPreferenceClass.isPopupActive,true);
        Utilclass.isSavePopActive=true;
        dialog=new SavePopupDialog(getActivity());
        dialog.setPopupCallBackFragmentInterface(popupCallBackFragmentInterface);
        dialog.setGameViewInterFace(gameViewInterface);
        dialog.setCancelable(false);
        dialog.show();
    }

/*
    public void setFragmentActionListener3(FragmentActionListener fragmentActionListener1){
        this.fragmentActionListener=fragmentActionListener1;
    }
    public GameViewInterface getGameViewInterface1(){
        return gameViewInterface;
    }

*/
}
