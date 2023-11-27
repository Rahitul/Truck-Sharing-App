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
import com.example.projectmove.Activity.HistoryForDriverActivity;
import com.example.projectmove.Activity.HomeActivity;
import com.example.projectmove.Activity.MainActivity;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterHistory;
import com.example.projectmove.Utilis.Class.History;
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


public class HistoryMainFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseAuth auth;
    List<History> HistoryList;
    AdapterHistory adapterHistory;

    String uid,customerPhone;
    FirebaseUser user;

    ExtendedFloatingActionButton fabHistoryAsDriver;
    LottieAnimationView progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_main, container, false);

        recyclerView=view.findViewById(R.id.historyRecyclerView);
        fabHistoryAsDriver=view.findViewById(R.id.fabHistoryAsDriver);
        progressBar=view.findViewById(R.id.progressBarHistoryMain);
        progressBar.setVisibility(View.VISIBLE);

        fabHistoryAsDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), HistoryForDriverActivity.class);
                startActivity(intent);
            }
        });

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        customerPhone=auth.getCurrentUser().getPhoneNumber();


        checkUserStatus();
        loadHistoryCustomer();

        return view;
    }

    private void loadHistoryCustomer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        HistoryList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("History");

        Query query = ref.orderByChild("customeruid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HistoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    History History = ds.getValue(History.class);

                    HistoryList.add(History);

                    adapterHistory = new AdapterHistory(getActivity(), HistoryList);

                    recyclerView.setAdapter(adapterHistory);


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