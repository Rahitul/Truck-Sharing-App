package com.example.projectmove.Messaging;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.projectmove.Activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import com.example.projectmove.R;

public class FirebaseService extends FirebaseMessagingService  {

    private final String CHANNEL_ID="channel_id";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Intent intent=new Intent(this, HomeActivity.class);
        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId= new Random().nextInt();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            createNotificationChannel(manager);
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivities(this,0,new Intent[] {intent}, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification;

            notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle(message.getData().get("title"))
                    .setContentText(message.getData().get("message"))
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent)
                    .build();
        manager.notify(notificationId,notification);
    }



    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager) {
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"channelName",NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Description");
        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);

        manager.createNotificationChannel(channel);
    }
}
