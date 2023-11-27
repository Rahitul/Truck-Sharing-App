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
import com.example.projectmove.Utilis.Adapter.AdapterHistory;
import com.example.projectmove.Utilis.Class.History;
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

public class HistoryForDriverActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth auth;
    List<History> HistoryList;
    AdapterHistory adapterHistory;

    String uid, customerPhone;
    FirebaseUser user;

    ImageView backDriverHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_for_driver);

        recyclerView = findViewById(R.id.historyRecyclerViewAsDriver);
        backDriverHistory=findViewById(R.id.backDriverHistory);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        customerPhone = auth.getCurrentUser().getPhoneNumber();


        checkUserStatus();
        loadHistoryCustomer();

        backDriverHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadHistoryCustomer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        HistoryList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("History");

        Query query = ref.orderByChild("driveruid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HistoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    History History = ds.getValue(History.class);

                    HistoryList.add(History);

                    adapterHistory = new AdapterHistory(getApplicationContext(), HistoryList);

                    recyclerView.setAdapter(adapterHistory);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), " Logged Out ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();


        } else {
            startActivity(new Intent(HistoryForDriverActivity.this, MainActivity.class));
            //getActivity().finish();
        }
    }
}