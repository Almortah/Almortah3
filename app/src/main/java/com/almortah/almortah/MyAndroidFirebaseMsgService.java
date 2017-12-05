package com.almortah.almortah;

/**
 * Created by ziyadalkhonein on 11/27/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
       // createNotification(remoteMessage.getNotification().getBody());
        notifyUser(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());

    }

    private void notifyUser(String from, String notification ) {
        MyNotificationManager notificationManager= new MyNotificationManager(getApplicationContext());
        notificationManager.showNotification(from,notification,new Intent(getApplicationContext(),ApproveBookingByOwner.class));


    }
}