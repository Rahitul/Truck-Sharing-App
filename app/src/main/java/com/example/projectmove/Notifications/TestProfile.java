package com.example.projectmove.Notifications;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectmove.Activity.MainActivity;
import com.example.projectmove.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;


public class TestProfile extends AppCompatActivity {

    String myPhone,myUid;

    public static final String NODE_USERS = "users";
    private static final String TAG = "MyActivity";
    private FirebaseAuth mAuth;
    private List<TestUser> userList;

    SwitchCompat postSwitch;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private static final String TOPIC_POST_NOTIFICATION="POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_profile);

        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String tokenS = task.getResult();
                            Toast.makeText(TestProfile.this, tokenS, Toast.LENGTH_SHORT).show();
                        } else {

                        }
                    }
                });*/


        checkUserStatus();

        postSwitch=findViewById(R.id.postSwitch);

        sp=getSharedPreferences("Notification_SP",MODE_PRIVATE);
        boolean isPostEnabled=sp.getBoolean(""+TOPIC_POST_NOTIFICATION,false);

        if(isPostEnabled){
            postSwitch.setChecked(true);
        }
        else{
            postSwitch.setChecked(false);
        }

        postSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                editor=sp.edit();
                editor.putBoolean(""+TOPIC_POST_NOTIFICATION,b);
                editor.apply();

                if(b){
                    subscribePostNotification();
                }
                else{
                    unsubscribePostNotification();
                }
            }
        });



       /*click.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               FirebaseDatabase database = FirebaseDatabase.getInstance();
               DatabaseReference tokensRef = database.getReference("users").child("token");

// Query for the first three tokens
               Query query = tokensRef.limitToFirst(3);
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       List<String> tokensList = new ArrayList<>();
                       for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                           String token = childSnapshot.getValue(String.class);
                           tokensList.add(token);
                       }

                       // Build notification data
                       Map<String, String> data = new HashMap<>();
                       data.put("title", "Notification Title");
                       data.put("body", "Notification Body");

                       // Build and send notification to all tokens
                       FirebaseMessaging messaging = FirebaseMessaging.getInstance();
                       for (String token : tokensList) {
                           RemoteMessage message = new RemoteMessage.Builder(token)
                                   .setData(data)
                                   .build();
                           try {
                               messaging.send(message);
                           } catch (Exception e) {
                               Log.e("TAG", "Error sending message to " + token, e);
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                       Log.e("TAG", "Database error: " + error.getMessage());
                   }
               });
           }
       });*/


        //NotificationHelper.displayNotification(this,"title","body");

        //FirebaseMessaging.getInstance().subscribeToTopic("Reject");

        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String tokenS = task.getResult();
                            saveToken(tokenS);
                        } else {

                        }
                    }
                });*/




    }

    private void unsubscribePostNotification() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        String msg="You will not receive post notification";
                        if(!task.isSuccessful()){
                            msg="Unsubscription failed";
                        }
                        Toast.makeText(TestProfile.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void subscribePostNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        String msg="You will receive post notification";
                        if(!task.isSuccessful()){
                            msg="Subscription failed";
                        }
                        Toast.makeText(TestProfile.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToken(String tokenS) {

        String phoneMessaging=myPhone;
        TestUser user=new TestUser(phoneMessaging,tokenS);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(NODE_USERS);

        databaseReference.child(myPhone).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(TestProfile.this, "Token Saved", Toast.LENGTH_SHORT).show();
                }

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
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
}


    }
