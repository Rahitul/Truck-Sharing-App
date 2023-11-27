package com.example.projectmove.Fragment;

import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectmove.Activity.HistoryRunningForDriverActivity;
import com.example.projectmove.Activity.MainActivity;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterRunningHistory;
import com.example.projectmove.Utilis.Class.RunningHistory;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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


public class HistoryRunningFragment extends Fragment {


    FirebaseAuth auth;
    List<RunningHistory> runningHistoryList;
    AdapterRunningHistory adapterRunningHistory;
    RecyclerView recyclerViewDriver,recyclerViewCustomer;

    String uid,customerPhone;
    FirebaseUser user;
    ExtendedFloatingActionButton fabRunningHistoryAsDriver;
    LottieAnimationView progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_running, container, false);

        recyclerViewCustomer=view.findViewById(R.id.runningHistoryCustomer);
        fabRunningHistoryAsDriver=view.findViewById(R.id.fabRunningHistoryAsDriver);
        progressBar=view.findViewById(R.id.progressBarRunningHistoryMain);
        progressBar.setVisibility(View.VISIBLE);

        fabRunningHistoryAsDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), HistoryRunningForDriverActivity.class);
                startActivity(intent);
            }
        });

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        customerPhone=auth.getCurrentUser().getPhoneNumber();


        checkUserStatus();
        loadRunningHistoryCustomer();




        return view;

    }






    private void loadRunningHistoryCustomer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerViewCustomer.setLayoutManager(layoutManager);

        runningHistoryList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RunningHistory");

        Query query = ref.orderByChild("customeruid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                runningHistoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    RunningHistory runningHistory = ds.getValue(RunningHistory.class);

                    runningHistoryList.add(runningHistory);

                    adapterRunningHistory = new AdapterRunningHistory(getActivity(), runningHistoryList);

                    recyclerViewCustomer.setAdapter(adapterRunningHistory);


                }
                progressBar.setVisibility(View.GONE);
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
            startActivity(new Intent((getActivity()), MainActivity.class));
            getActivity().finish();
        }

    }
}