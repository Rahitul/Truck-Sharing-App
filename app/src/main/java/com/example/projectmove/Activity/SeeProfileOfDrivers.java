package com.example.projectmove.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterAboutDrivers;
import com.example.projectmove.Utilis.Adapter.AdapterComments;
import com.example.projectmove.Utilis.Class.AboutDrivers;
import com.example.projectmove.Utilis.Class.Comments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeProfileOfDrivers extends AppCompatActivity {

    CircleImageView profileImage;
    TextView userName,seeRating,seeProfilesVerify;
    RatingBar seeRatingStars;
    ImageView back;

    FirebaseStorage storage;
    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseDatabase database;

    DatabaseReference myRef,ratingRef;

    String driveruid,driverPhone,ratingValue,phone;
    RecyclerView recyclerView;

    List<AboutDrivers> aboutDriversList;
    AdapterAboutDrivers adapterAboutDrivers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile_of_drivers);

        back=findViewById(R.id.backSeeDriverProfile);
        profileImage=findViewById(R.id.imageSeeDriverProfile);
        userName=findViewById(R.id.usernameSeeDriverProfile);
        seeRating=findViewById(R.id.tv_rating_see_driver_profile);
        seeRatingStars=findViewById(R.id.rating_bar_see_driver_profile);

        recyclerView=findViewById(R.id.recyclerViewAboutDrivers);
        seeProfilesVerify=findViewById(R.id.seeProfilesVerifyTV);

        database= FirebaseDatabase.getInstance();

        ratingRef=database.getReference("Rating");
        myRef=database.getReference("Users");

        Intent intent=getIntent();
        driveruid=intent.getStringExtra("driveruid");
        driverPhone=intent.getStringExtra("driverPhone");

        loadComments();


        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        phone = auth.getCurrentUser().getPhoneNumber();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        //loadRating
        ratingRef.child(driveruid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                    if(map.get("value")!=null){
                        ratingValue=map.get("value").toString();

                        String formattedString = ratingValue.substring(0, 3);

                        seeRating.setText(formattedString);

                        float ratingValueFloat = Float.parseFloat(ratingValue);
                        seeRatingStars.setRating(ratingValueFloat);
                        seeRatingStars.setIsIndicator(true);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        //loadImage
        myRef.child(driverPhone).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);

                if(value!=null){


                    Picasso.get().load(value).into(profileImage);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        //loadUserName
        myRef.child(driverPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                    if(map.get("username")!=null){
                        String mName=map.get("username").toString();
                        userName.setText(mName);
                    }

                    if(map.get("isVerified")!=null){
                        String mName=map.get("isVerified").toString();
                        seeProfilesVerify.setText(mName);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadComments() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        aboutDriversList=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("AboutDrivers").child(driveruid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aboutDriversList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    AboutDrivers aboutDrivers=ds.getValue(AboutDrivers.class);

                    aboutDriversList.add(aboutDrivers);

                    adapterAboutDrivers=new AdapterAboutDrivers(getApplicationContext(),aboutDriversList);

                    recyclerView.setAdapter(adapterAboutDrivers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}