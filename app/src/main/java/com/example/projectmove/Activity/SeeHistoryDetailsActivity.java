package com.example.projectmove.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeHistoryDetailsActivity extends AppCompatActivity {
    TextView HCustomerName,HCustomerPhone,HDriverName,HDriverPhone
            ,HPostPerssonName,HPostLocation,HPostDestination,HPostStartingTime,HPostStartingDate,HExpectedPrice;
    CircleImageView HCustomerDp,HDriverDp;

    String rHId;
    String customerDp,driverDp;


    String customeruid,driverPostId,driveruid,isAId,isAccepted,pTime,timestamp,currentTime;

    String customerName,customerPhone,driverName,driverPhone,pDName,pDLocation,pDDestination,pDStartingTime,pDStartingDate,
            pDExpectedPrice;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_history_details);

        currentTime = String.valueOf(System.currentTimeMillis());
        timestamp = currentTime;
        isAId = currentTime;

        HCustomerName = findViewById(R.id.HCustomerName);
        HCustomerPhone = findViewById(R.id.HCustomerPhone);
        HCustomerDp = findViewById(R.id.HCustomerDp);

        HDriverName = findViewById(R.id.HDriverName);
        HDriverPhone = findViewById(R.id.HDriverPhone);
        HDriverDp = findViewById(R.id.HDriverDp);

        HPostPerssonName = findViewById(R.id.HPostPerssonName);
        HPostLocation = findViewById(R.id.HPostLocation);
        HPostDestination = findViewById(R.id.HPostDestination);
        HPostStartingTime = findViewById(R.id.HPostStartingTime);
        HPostStartingDate = findViewById(R.id.HPostStartingDate);
        HExpectedPrice = findViewById(R.id.HExpectedPrice);


        Intent intent = getIntent();
        rHId = intent.getStringExtra("rHId");


        //loadRunningHistoryDetails
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("History");
        Query query = ref.orderByChild("timestamp").equalTo(rHId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    customerName = "" + ds.child("customerName").getValue();
                    customerPhone = "" + ds.child("customerPhone").getValue();
                    driverName = "" + ds.child("driverName").getValue();
                    driverPhone = "" + ds.child("driverPhone").getValue();
                    customerDp = "" + ds.child("customerDp").getValue(String.class);
                    driverDp = "" + ds.child("driverDp").getValue(String.class);
                    pDName = "" + ds.child("pDName").getValue();
                    pDLocation = "" + ds.child("pDLocation").getValue();
                    pDDestination = "" + ds.child("pDDestination").getValue();
                    pDStartingTime = "" + ds.child("pDStartingTime").getValue();
                    pDStartingDate = "" + ds.child("pDStartingDate").getValue();
                    pDExpectedPrice = "" + ds.child("pDExpectedPrice").getValue();
                    customeruid = "" + ds.child("customeruid").getValue();
                    driverPostId = "" + ds.child("driverPostId").getValue();
                    driveruid = "" + ds.child("driveruid").getValue();
                    isAccepted = "" + ds.child("isAccepted").getValue();
                    pTime = "" + ds.child("pTime").getValue();

                    HCustomerName.setText(customerName);
                    HCustomerPhone.setText(customerPhone);
                    HDriverName.setText(driverName);
                    HDriverPhone.setText(driverPhone);
                    HPostLocation.setText(pDLocation);
                    HPostDestination.setText(pDDestination);
                    HPostStartingTime.setText(pDStartingTime);
                    HPostStartingDate.setText(pDStartingDate);
                    HPostPerssonName.setText(pDName);
                    HExpectedPrice.setText(pDExpectedPrice);

                    try {
                        Picasso.get().load(customerDp).placeholder(R.drawable.avatar).into(HCustomerDp);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.avatar).into(HCustomerDp);
                    }

                    try {
                        Picasso.get().load(driverDp).placeholder(R.drawable.avatar).into(HDriverDp);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.avatar).into(HDriverDp);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveToHistory() {




    }
    }
