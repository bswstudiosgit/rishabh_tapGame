package com.rishabhmatharoo.blacklight.FirebaseCloudMessageService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rishabhmatharoo.blacklight.Activity.Activity_Main;
import com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model.DataClass;
import com.rishabhmatharoo.blacklight.FirebaseCloudMessageService.Model.PayloadData;
import com.rishabhmatharoo.blacklight.Preference.SharedPreferenceClass;
import com.rishabhmatharoo.blacklight.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private PayloadData payloadData;
    private String key1="Name";
    private String key2="Msg";
    private String key3="msgType";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Get Title and body
        Log.d("FirebaseMessaging","FirebaseNotification");
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //if(remoteMessage.getData().size()>0){
              //  String str=convertHasmapIntoJsonString(remoteMessage.getData());
               // sendNotification(remoteMessage.getNotification().getBody(),str);
            //}else{
                sendNotification(remoteMessage.getNotification().getBody(),null);
            //}
            return;
        }
        sendPayloadMessage(remoteMessage.getData());
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody,String PayloadMssg) {
    Log.d("FirebaseNoification",messageBody);
        Intent intent = new Intent(this, Activity_Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if(PayloadMssg!=null){
            intent.putExtra("data",PayloadMssg);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private String convertHasmapIntoJsonString(Map<String,String> data){

        Map<String, String> elements = data;
        Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(elements,gsonType);
        //Log.d("PayLoad",gsonString);
        return gsonString;
    }
    private void sendPayloadMessage(Map<String,String> map){
        try {
            //String jsonvalue = remoteMessage.getData().get("message");
            //Log.d("Payload",jsonvalue);
            String jsonString=convertHasmapIntoJsonString(map);
            //String json= jsonvalue;
            DataClass dataClass = new Gson().fromJson(jsonString, DataClass.class);
            Log.d("Payload",dataClass.Msg);
            if (dataClass.msgType.equals("1")) {
                    sendNotification(dataClass.Msg,jsonString);
            } else if (dataClass.msgType.equals("2")) {
                SharedPreferenceClass.getInstance(getApplicationContext()).setDataPayload(jsonString);
                SharedPreferenceClass.getInstance(getApplicationContext()).setDataPayloadboolean(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
