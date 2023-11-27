package com.example.projectmove.Activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterRunningHistory;
import com.example.projectmove.Utilis.Class.RunningHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryRunningForDriverActivity extends AppCompatActivity {

    FirebaseAuth auth;
    List<RunningHistory> runningHistoryList;
    AdapterRunningHistory adapterRunningHistory;
    RecyclerView recyclerViewDriver;

    String uid,customerPhone;
    FirebaseUser user;

    ImageView backDriverRunningHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_running_for_driver);

        recyclerViewDriver=findViewById(R.id.runningHistoryDriver);
        backDriverRunningHistory=findViewById(R.id.backDriverRunningHistory);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        customerPhone=auth.getCurrentUser().getPhoneNumber();


        checkUserStatus();
        loadRunningHistoryCustomer();
        backDriverRunningHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





    }






    private void loadRunningHistoryCustomer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerViewDriver.setLayoutManager(layoutManager);

        runningHistoryList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RunningHistory");

        Query query = ref.orderByChild("driveruid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                runningHistoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    RunningHistory runningHistory = ds.getValue(RunningHistory.class);

                    runningHistoryList.add(runningHistory);

                    adapterRunningHistory = new AdapterRunningHistory(getApplicationContext(), runningHistoryList);

                    recyclerViewDriver.setAdapter(adapterRunningHistory);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), " Logged Out ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus(){

        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            uid=user.getUid();



        }

        else{
            Intent intent=new Intent(HistoryRunningForDriverActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    }
