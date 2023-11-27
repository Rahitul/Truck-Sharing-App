package com.example.projectmove.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterCustomersJobs;
import com.example.projectmove.Utilis.Adapter.AdapterNotifications;
import com.example.projectmove.Utilis.Class.CustomersJobs;
import com.example.projectmove.Utilis.Class.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    RecyclerView recyclerView;
    List<Notifications> notificationsList;
    AdapterNotifications adapterNotifications;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView=view.findViewById(R.id.recycleviewNotifications);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        String uid=user.getUid();


        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        notificationsList=new ArrayList<>();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Notifications").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationsList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Notifications notifications =ds.getValue(Notifications.class);

                    notificationsList.add(notifications);

                    adapterNotifications=new AdapterNotifications(getActivity(),notificationsList);
                    recyclerView.setAdapter(adapterNotifications);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        return view;
    }
}