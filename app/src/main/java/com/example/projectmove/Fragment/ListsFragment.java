package com.example.projectmove.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.projectmove.Activity.AmbulanceActivity;
import com.example.projectmove.Activity.CustomerActivity;
import com.example.projectmove.Activity.DriverActivity;
import com.example.projectmove.Notifications.TestProfile;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;


import java.nio.channels.MulticastChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;

public class ListsFragment extends Fragment {

    LinearLayout needDriver,amDriver,needAmbulance;
    CircleImageView imageView;
    FirebaseStorage storage;
    FirebaseAuth auth;
    TextView username;

    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button testT;
    private ArrayList<Integer> images;
    private int currentPosition = 0;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_lists, container, false);

        needDriver=view.findViewById(R.id.needDriver);
        amDriver=view.findViewById(R.id.amDriver);
        needAmbulance=view.findViewById(R.id.needAmbulance);
        imageView=view.findViewById(R.id.showDp);
        username=view.findViewById(R.id.showUserName);

        testT=view.findViewById(R.id.testT);

        images = new ArrayList<>();
        images.add(R.drawable.img_1);
        images.add(R.drawable.img_2);
        images.add(R.drawable.img_3);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), images);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);



        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");

        testT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TestProfile.class);
                startActivity(intent);

            }
        });





        needDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomerActivity.class);
                startActivity(intent);
            }
        });

        amDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DriverActivity.class);
                startActivity(intent);
            }
        });

        needAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AmbulanceActivity.class);
                startActivity(intent);
            }
        });


        String phone = auth.getCurrentUser().getPhoneNumber();



        myRef.child(phone).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);

                if(value!=null){


                    Picasso.get().load(value).into(imageView);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                    if(map.get("username")!=null){
                        String mName=map.get("username").toString();
                        username.setText(mName);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }


        private void startSlider() {
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    currentPosition++;
                    if (currentPosition == images.size()) {
                        currentPosition = 0;
                    }
                    imageView.setImageResource(images.get(currentPosition));
                    handler.postDelayed(this, 3000); // Change image every 3 seconds
                }
            };
            handler.postDelayed(runnable, 3000); // Start the handler
    }
}