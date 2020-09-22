package com.rishabhmatharoo.blacklight.CustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.rishabhmatharoo.blacklight.Activity_Main;
import com.rishabhmatharoo.blacklight.AdHandler.AdMobHandler;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Interfaces.PopupCallBackFragmentInterface;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.Util.Utilclass;

public class RewardAdPopupDialog extends Dialog {
    private Activity activity;
    private Button watch,cancel;
    FragmentActionListener fragmentActionListener;
    public RewardAdPopupDialog(Activity activity){
        super(activity);
        this.activity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activity instanceof Activity_Main) {
            fragmentActionListener = ((FragmentActionListener) activity);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reward_ad_popup);
        try {
            ViewGroup.LayoutParams params = getWindow().getAttributes();
            if (params != null) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        watch=findViewById(R.id.reward_watch);
        cancel=findViewById(R.id.reward_cancel);

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                AdMobHandler.getInstance(activity).showRewardAd();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GameOver Fragment.
                dismiss();
                Bundle bundle = new Bundle();
                if (fragmentActionListener != null ) {

                    bundle.putString(FragmentActionListener.FRAGMENT_NAME, "GameOver");

                    bundle.putInt("FinalScore", Utilclass.finalScoreDuringRewardAd);

                    fragmentActionListener.onFragmentSelected(bundle);
                }
            }
        });
    }

}
