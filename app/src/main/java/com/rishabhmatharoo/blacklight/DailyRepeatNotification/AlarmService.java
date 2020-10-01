package com.rishabhmatharoo.blacklight.DailyRepeatNotification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class AlarmService {
    private static AlarmService service;
    private Context context;
    private AlarmService(Context context){
        this.context=context;
    }
    public synchronized static AlarmService getInstance(Context context){
        if(service==null){
            service=new AlarmService(context);
        }
        return service;
    }
    public void setDailyNotification(){

        Log.d("DailyNotification","Triggered");
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,19);
        calendar.set(Calendar.MINUTE,9);
        Intent intent=new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Log.d("DailyNotification","Triggered");
    }
}
