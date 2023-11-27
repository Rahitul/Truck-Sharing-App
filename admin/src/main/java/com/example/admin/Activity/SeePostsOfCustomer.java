package com.example.admin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.admin.Adapter.AdapterCustomersJobs;
import com.example.admin.Class.CustomersJobs;
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

public class SeePostsOfCustomer extends AppCompatActivity {

    RecyclerView customersPostsRecyclerView;
    List<CustomersJobs> customersJobLists;
    AdapterCustomersJobs adapterCustomersJobs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_posts_of_customer);

        customersPostsRecyclerView=findViewById(R.id.postsOfCustomer);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        customersPostsRecyclerView.setLayoutManager(layoutManager);

        customersJobLists=new ArrayList<>();

        loadPosts();
    }

    private void loadPosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customersJobLists.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    CustomersJobs customersJobs =ds.getValue(CustomersJobs.class);

                    customersJobLists.add(customersJobs);

                    adapterCustomersJobs=new AdapterCustomersJobs(getApplicationContext(),customersJobLists);
                    customersPostsRecyclerView.setAdapter(adapterCustomersJobs);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}