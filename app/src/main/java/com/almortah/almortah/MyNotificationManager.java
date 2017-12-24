package com.almortah.almortah;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ziyadalkhonein on 11/28/17.
 */

public class MyNotificationManager {
    private Context context;
    public static final int Notification_ID = 234;

    public MyNotificationManager(Context context){
        this.context=context;
    }
    public void showNotification(String from , String notifcation, Intent intent){

        Log.i("Context",context.getPackageName());
        Log.i("Context",context.getPackageCodePath());
        Log.i("ziyad","hello");

        PendingIntent pendingIntent=PendingIntent.getActivity(
                context,
                Notification_ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("AL-Mortah")
                .setContentText(notifcation)
                .setSound(defaultSoundUri)
                .setAutoCancel( true )
                ;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationBuilder.setContentIntent(pendingIntent);

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(ns);

        notificationManager.notify(Notification_ID, mNotificationBuilder.build());



    }
}
