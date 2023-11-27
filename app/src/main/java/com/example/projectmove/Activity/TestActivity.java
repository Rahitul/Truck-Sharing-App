package com.example.projectmove.Activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.iid.FirebaseInstanceIdReceiver;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity {


    TextView timerOne;
    DatabaseReference ref;
    String myPhone,myUid;
    String dataTree = "dataTree";
    EditText f,s;
    private String userId;

    public static  final String CHANNEL_ID="simplified_coding";
    private static  final String CHANNEL_NAME="Simplified Coding";
    private static  final String CHANNEL_DESC="Simplified Coding Notifications";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        timerOne = findViewById(R.id.timerOne);
        f=findViewById(R.id.first_et);
        s=findViewById(R.id.second_et);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        timerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNotification();
            }
        });


        /*FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token=task.getResult().getToken();
                            timerOne.setText("Token: "+token);
                        }
                        else {
                            timerOne.setText("Token Not Generated");
                        }
                    }
                });*/

        

        timerOne=findViewById(R.id.timerOne);
        checkUserStatus();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");
        Query query=ref.orderByChild("cUId").equalTo(myUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    String timer = "" + ds.child("timer").getValue(long.class);
                    long timeInMillis = Long.parseLong(timer);
                    long timeLeft = timeInMillis - System.currentTimeMillis();
                    String timeLeftString = String.format("%d min to end", TimeUnit.MILLISECONDS.toMinutes(timeLeft));
                    timerOne.setText(timeLeftString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkUserStatus(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            myPhone=user.getPhoneNumber();
            myUid=user.getUid();

        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            timerOne.setText("Token: " + token);
                        } else {
                            timerOne.setText("Token Not Generated");
                        }
                    }
                });*/




    }

    private void displayNotification(){
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_emoji_people_24)
                .setContentTitle("Hurray!! It is working")
                .setContentText("Your First Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,mBuilder.build());

    }

}