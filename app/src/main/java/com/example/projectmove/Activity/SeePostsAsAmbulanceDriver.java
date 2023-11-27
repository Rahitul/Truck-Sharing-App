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
import com.example.projectmove.Utilis.Adapter.AdapterAmbulanceJobs;
import com.example.projectmove.Utilis.Adapter.AdapterDriversJobs;
import com.example.projectmove.Utilis.Class.AmbulanceJobs;
import com.example.projectmove.Utilis.Class.DriversJobs;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SeePostsAsAmbulanceDriver extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AmbulanceJobs> ambulanceJobsList = new ArrayList<>();
    AdapterAmbulanceJobs adapterAmbulanceJobs;

    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageRaference;
    ImageView backSeePostsAsAmbulance;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_posts_as_ambulance_driver);

        backSeePostsAsAmbulance=findViewById(R.id.backSeePostsAsAmbulance);
        recyclerView=findViewById(R.id.recycleviewAmbulancePosts);

        backSeePostsAsAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");


        checkUserStatus();
        loadMyPosts();
    }

    private void loadMyPosts() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("AmbulancePosts");

        Query query=ref.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ambulanceJobsList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    AmbulanceJobs ambulanceJobs =ds.getValue(AmbulanceJobs.class);

                    ambulanceJobsList.add(ambulanceJobs);

                    adapterAmbulanceJobs=new AdapterAmbulanceJobs(getApplicationContext(), ambulanceJobsList);

                    recyclerView.setAdapter(adapterAmbulanceJobs);


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
            startActivity(new Intent((getApplicationContext()), MainActivity.class));
            getApplicationContext();
            finish();
        }
}
}