package com.almortah.almortah;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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

        PendingIntent pendingIntent=PendingIntent.getActivity(
                context,
                Notification_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("AL-Mortah")
                .setContentText(notifcation)
                .setAutoCancel( true )
                ;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationBuilder.setContentIntent(pendingIntent);

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(ns);

        notificationManager.notify(Notification_ID, mNotificationBuilder.build());



    }
}
