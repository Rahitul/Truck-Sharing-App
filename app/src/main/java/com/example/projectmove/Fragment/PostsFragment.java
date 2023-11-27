package com.example.projectmove.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectmove.Activity.MainActivity;
import com.example.projectmove.Activity.SeePostsAsCustomer;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterCustomersJobs;
import com.example.projectmove.Utilis.Adapter.AdapterDriversJobs;
import com.example.projectmove.Utilis.Class.CustomersJobs;
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


public class PostsFragment extends Fragment {

    RecyclerView recycleviewCustomerPosts;
    List<CustomersJobs> customersJobsList = new ArrayList<>();
    AdapterCustomersJobs adapterCustomersJobs;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;

    String uid;

    ExtendedFloatingActionButton fabSeePostsAsCustomer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_posts, container, false);


        recycleviewCustomerPosts=view.findViewById(R.id.recycleviewDriverPosts);
        fabSeePostsAsCustomer=view.findViewById(R.id.fabSeePostsAsCustomer);

        fabSeePostsAsCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SeePostsAsCustomer.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");


        checkUserStatus();
        loadCustomersPosts();



        return view;


    }

    private void loadCustomersPosts() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recycleviewCustomerPosts.setLayoutManager(layoutManager);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");

        Query query=ref.orderByChild("cUId").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customersJobsList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    CustomersJobs customersJobs =ds.getValue(CustomersJobs.class);

                    customersJobsList.add(customersJobs);

                    adapterCustomersJobs=new AdapterCustomersJobs(getActivity(),customersJobsList);

                    recycleviewCustomerPosts.setAdapter(adapterCustomersJobs);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), " Logged Out ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUserStatus(){

        FirebaseUser user=auth.getCurrentUser();
        if(user!=null){
            uid=user.getUid();
        }

        else{
            Intent intent=new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }


    }
}