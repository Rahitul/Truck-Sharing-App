package com.example.admin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.admin.Adapter.AdapterDriversJobs;
import com.example.admin.Class.DriversJobs;
import com.example.admin.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SeePostsOfDriver extends AppCompatActivity {

    List<DriversJobs> driversJobLists;
    AdapterDriversJobs adapterDriversJobs;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_posts_of_driver);

        recyclerView=findViewById(R.id.postOfDriver);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        driversJobLists=new ArrayList<>();

        loadPosts();
    }

    private void loadPosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                driversJobLists.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    DriversJobs driversJobs =ds.getValue(DriversJobs.class);

                    driversJobLists.add(driversJobs);

                    adapterDriversJobs=new AdapterDriversJobs(getApplicationContext(),driversJobLists);
                    recyclerView.setAdapter(adapterDriversJobs);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}