package com.example.admin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.admin.Adapter.AdapterAllUsers;
import com.example.admin.Adapter.AdapterDriversJobs;
import com.example.admin.Class.AllUsers;
import com.example.admin.Class.DriversJobs;
import com.example.admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SeeAllUsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AllUsers> allUsersList;
    AdapterAllUsers adapterAllUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_users);

        recyclerView=findViewById(R.id.allUserView);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        allUsersList=new ArrayList<>();

        loadAllUsers();
    }

    private void loadAllUsers() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUsersList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    AllUsers allUsers =ds.getValue(AllUsers.class);

                    allUsersList.add(allUsers);

                    adapterAllUsers=new AdapterAllUsers(getApplicationContext(),allUsersList);
                    recyclerView.setAdapter(adapterAllUsers);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}