package com.rishabhmatharoo.blacklight.Preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceClass {
    private static SharedPreferenceClass instance;
    public  static final String BestScore = "bestscore";
    public  static final String savegamescore="SaveGameScore";
    public  static final String isSave="isSave";
    public static  final String colornum="colornum";
    public static final String language="languagetype";
    public  SharedPreferences mSharedPref;
    public static String isPopupActive="IsPopupActive";
    public static String handlertime="HandlerTimer";
    public static String colorvalues="ColorValue";
    public static String BgMusic="BgMusic";
    public static String interstitialFreq="InterstitialFrequency";
    public static String playerExperience="PlayerExperience";
    public static String ColorDefaultValues="{\"colour1\":\"#ff0000\",\"colour2\":\"#00ff00\",\"colour3\":\"#0000ff\",\"colour4\":\"#FFFF00\"}";
    public static String wrongkeyofbestscore="wrong";
    public static String dataPayloadValue="DataPayLoad";
    public static String dailyNotification="DailyNotification";
    public static String hasPayLoadValue="hasPayloadValue";
private SharedPreferenceClass(Context context)
    {

      mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        Log.d("SharedPrefreence","times");
    }

    public static synchronized SharedPreferenceClass getInstance(Context context)
    {    if(instance == null) {
            instance = new SharedPreferenceClass(context);
        }
        return instance;
    }


    public Integer read(String key) {

        return mSharedPref.getInt(key, 0);
    }
    public String readnullvalue(String key){
        return mSharedPref.getString(key, null);
    }
    public boolean readboolean(String key) {

        return mSharedPref.getBoolean(key, false);
    }
    public void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }
    public void writeBoolean(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value).apply();
    }
    public String readColorValue(){
        return mSharedPref.getString(colorvalues,ColorDefaultValues);

    }
    public void writeColorValues(String value){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(colorvalues, value).apply();
    }
    public String readHandlerTimer(){
        return mSharedPref.getString(handlertime,"1000");
    }

    public void writeHandlerTimer(String val){
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(handlertime, val).apply();
    }
    public String readBgMusic(){
        return mSharedPref.getString(BgMusic,"1");
    }
    public void writeBgMusic(String value){
    SharedPreferences.Editor prefsEditor=mSharedPref.edit();
    prefsEditor.putString(BgMusic,value).apply();
    }
    public void writeInterstitialFrequency(String value){
        SharedPreferences.Editor prefsEditor=mSharedPref.edit();
        prefsEditor.putString(interstitialFreq,value).apply();
    }
    public String readInterstitialFrequency(){
        return mSharedPref.getString(interstitialFreq,"2");
    }
    public void setDataPayload(String Val){
        SharedPreferences.Editor prefsEditor=mSharedPref.edit();
        prefsEditor.putString(dataPayloadValue,Val).apply();
    }
    public String getDataPayloadValue(){
        return mSharedPref.getString(dataPayloadValue,"");
    }

    public void setDataPayloadboolean(boolean val){
        SharedPreferences.Editor prefsEditor=mSharedPref.edit();
        prefsEditor.putBoolean(hasPayLoadValue,val).apply();
    }
    public boolean getDataPayloadboolean(){
        return mSharedPref.getBoolean(hasPayLoadValue,false);
    }

    public void setDailyNotification(boolean val){
        SharedPreferences.Editor prefsEditor=mSharedPref.edit();
        prefsEditor.putBoolean(dailyNotification,val).apply();
    }
    public boolean getDailyNotificationBoolean(){
        return mSharedPref.getBoolean(dailyNotification,false);
    }
}
