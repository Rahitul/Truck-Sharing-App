package com.example.projectmove.Notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.projectmove.Activity.TestActivity;
import com.example.projectmove.R;

public class NotificationHelper {

    public static void displayNotification(Context context, String title,String body){

        Intent intent=new Intent(context,TestProfile.class);

        PendingIntent pendingIntent=PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context, TestActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_emoji_people_24)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mBuilder.build());

    }
}
