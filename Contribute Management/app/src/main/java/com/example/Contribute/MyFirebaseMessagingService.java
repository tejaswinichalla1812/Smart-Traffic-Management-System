package com.example.Contribute;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseService";

    /*
    start onNewToken
    There are two scenarios when onNewToken is called
    1-> When a new token is generated on initial app startup
    2-> Whenever an existing token is changed
        Under #2, there are three scenarios when the existing token is getChanged
       A-> App is restored to a new device
       B-> User uninstall/reinstall the app
       c-> User clears app data
            */

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed Token :" +token );

        //FCM registration to token to your app server
        //sendRegistrationToken(token);

    }

    // Called when the message is received
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d(TAG, "From :" + message.getFrom());

        if (message.getData().size() > 0) {
            Log.d(TAG, "Message Data Payload :" + message.getData());

            sendNotification(message.getData());
            if (true) {
                // Handle  Long Running jobs
                scheduleJobs();
            } else {
                // Handle Message within 10 Seconds
               handleNow();
            }
        }

        //check if message contains a notification payload
        if(message.getNotification()!=null){
            Log.d(TAG, "Message Notification Body :" + message.getData());
        }

    }
/*
    // Handle Time alloted to BoardCastReceivers*/
    private void handleNow() {
    Log.d(TAG,"Short lived Task is done");
    }

    /*Schedule async work using workmanager*/

    private void scheduleJobs() {
 //       OneTimeWorkRequest workRequest=new OneTimeWorkRequest.Builder(MyWorker.class).build();
   //     WorkManager.getInstance(this).beginWith(workRequest).enqueue();
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);
    }

    public void sendNotification(Map<String, String> messageBody){
        Intent intent=new Intent(this,GoodWillLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String channelID=getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder=
                new NotificationCompat.Builder(this,channelID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(messageBody.get("title"))
                .setContentText(messageBody.get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //since android oreo notification channel is needed
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(channelID
            ,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0,notificationBuilder.build());
    }
}
