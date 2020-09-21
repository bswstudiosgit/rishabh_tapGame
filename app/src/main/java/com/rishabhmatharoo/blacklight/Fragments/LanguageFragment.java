package com.rishabhmatharoo.blacklight.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rishabhmatharoo.blacklight.Activity_Main;
import com.rishabhmatharoo.blacklight.Interfaces.FragmentActionListener;
import com.rishabhmatharoo.blacklight.Adapter.MultiLangAdapter;
import com.rishabhmatharoo.blacklight.Interfaces.PopupCallBackFragmentInterface;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;
import com.rishabhmatharoo.blacklight.Interfaces.RecylerViewCallBack;

import java.util.ArrayList;

public class LanguageFragment extends Fragment implements RecylerViewCallBack {
    FragmentActionListener fragmentActionListener;
    ArrayList<String> listData=new ArrayList<>();
    MultiLangAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        return inflater.inflate(R.layout.multilang_fragment,parent,false);
    }

    public void setFragmentActionListener5(FragmentActionListener fragmentActionListener1){
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
    public void onViewCreated(View view,Bundle savedInstanceState) {
        //Setup any Handles to view objects here

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recylerviewmultilang);
        listData.add(getString(R.string.language1));
        listData.add(getString(R.string.language2));
        listData.add(getString(R.string.language6));
        listData.add(getString(R.string.language3));
        listData.add(getString(R.string.language4));
        listData.add(getString(R.string.language5));


        adapter=new MultiLangAdapter(listData,getActivity());
        adapter.setCallBack(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ImageView backbutton=(ImageView)view.findViewById(R.id.backbutton2);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentActionListener!=null && !isStateSaved()){
                    Bundle bundle=new Bundle();
                    bundle.putString(FragmentActionListener.FRAGMENT_NAME,"HomeScreen");
                    fragmentActionListener.onFragmentSelected(bundle);
                }
            }
        });

        }
        public void languagesettransaction(){
            if(fragmentActionListener!=null && !isStateSaved()){
                Bundle bundle=new Bundle();
                bundle.putString(FragmentActionListener.FRAGMENT_NAME,"HomeScreen");
                fragmentActionListener.onFragmentSelected(bundle);
            }

        }
    @Override
    public void changecallback() {
/*
        listData.clear();
        listData.add(getString(R.string.language1));
        listData.add(getString(R.string.language2));
        listData.add(getString(R.string.language6));

        listData.add(getString(R.string.language3));
        listData.add(getString(R.string.language4));
        listData.add(getString(R.string.language5));
        adapter.notifyDataSetChanged();
        if(SharedPreferenceClass.getInstance(getContext()).read(SharedPreferenceClass.language)==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
*/
    getActivity().recreate();
    }




}
