package com.example.projectmove.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.projectmove.Activity.EditProfileActivity;
import com.example.projectmove.Activity.MainActivity;
import com.example.projectmove.Activity.ProfileActivity;
import com.example.projectmove.R;
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
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView imageView;
    FirebaseStorage storage;
    FirebaseAuth auth;
    TextView username,address,nationalIdCardNo,logOut,phoneNumber;


    String uid;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef,ratingRef;
    StorageReference storageRaference;

    ExtendedFloatingActionButton fab;


    RatingBar rating_bar;
    TextView tv_rating,verify;

    String ratingValue;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        imageView=view.findViewById(R.id.ImageViewId);
        storage = FirebaseStorage.getInstance();
        username=view.findViewById(R.id.usernameTV);
        address=view.findViewById(R.id.addressTV);
        nationalIdCardNo=view.findViewById(R.id.nationalIdCardNoTV);
        logOut=view.findViewById(R.id.logOutTV);
        phoneNumber=view.findViewById(R.id.phoneNumber);

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");
        ratingRef=database.getReference("Rating");
        storageRaference=storage.getReference();

        phoneNumber.setText(user.getPhoneNumber());

        rating_bar=view.findViewById(R.id.rating_bar);
        tv_rating=view.findViewById(R.id.tv_rating);
        verify=view.findViewById(R.id.verifyTV);





        fab= view.findViewById(R.id.editProfile);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });




        //loadRating
        ratingRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                    if(map.get("value")!=null){
                        ratingValue=map.get("value").toString();

                        String formattedString = ratingValue.substring(0, 3);

                        tv_rating.setText(formattedString);

                        float ratingValueFloat = Float.parseFloat(ratingValue);
                        rating_bar.setRating(ratingValueFloat);
                        rating_bar.setIsIndicator(true);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        //StorageReference reference = storage.getReference().child("Profiles").child(auth.getCurrentUser().getPhoneNumber());


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

                    if(map.get("address")!=null){
                        String mName=map.get("address").toString();
                        address.setText(mName);
                    }

                    if(map.get("nationalIdCardNo")!=null){
                        String mName=map.get("nationalIdCardNo").toString();
                        nationalIdCardNo.setText(mName);
                    }

                    if(map.get("isVerified")!=null){
                        String mName=map.get("isVerified").toString();
                        verify.setText(mName);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return view;






    }

}