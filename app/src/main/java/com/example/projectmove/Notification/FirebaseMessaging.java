package com.example.projectmove.Notification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.projectmove.Activity.PostDriverActivity;
import com.example.projectmove.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseMessaging extends FirebaseMessagingService {

    private static final String ADMIN_CHANNEL_ID = "admin_channel";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);


        SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
        String savedCurrentUser=sp.getString("Current_USERID","None");


        String notificationType=message.getData().get("notificationType");
        if(notificationType.equals("PostNotification")){
            String pId=message.getData().get("pId");
            String sender=message.getData().get("sender");
            String pTitle=message.getData().get("pTitle");
            String pDescription=message.getData().get("pDescription");

            if(!sender.equals(savedCurrentUser)){
                showPostNotification(""+pId,""+pTitle,""+pDescription);
            }
        }

    }

    private void showPostNotification(String pId, String pTitle, String pDescription) {

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID=new Random().nextInt(3000);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            setUpPostNotificationChannel(notificationManager);


        }

        Intent intent=new Intent(this, PostDriverActivity.class);
        intent.putExtra("postId",pId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon= BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_emoji_people_24);

        Uri notificationSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this, ""+ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_emoji_people_24)
                .setLargeIcon(largeIcon)
                .setContentTitle(pTitle)
                .setContentText(pDescription)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationID,notificationBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setUpPostNotificationChannel(NotificationManager notificationManager) {

        CharSequence channelName="New Notification";
        String channelDescription="Device to device post notification";

        NotificationChannel adminChannel=new NotificationChannel(ADMIN_CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if(notificationManager!=null){
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
