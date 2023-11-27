package com.example.projectmove.Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeHistoryRunningDetailsActivity extends AppCompatActivity {

    TextView rHCustomerName,rHCustomerPhone,rHDriverName,rHDriverPhone
            ,rHPostPerssonName,rHPostLocation,rHPostDestination,rHPostStartingTime,rHPostStartingDate,rHExpectedPrice;
    CircleImageView rHCustomerDp,rHDriverDp;
    String rHId;
    String customerDp,driverDp;
    TextView rHFinishJob,cancelTrip;
    String customeruid,driverPostId,driveruid,isAId,isAccepted,pTime,timestamp,currentTime;
    String customerName,customerPhone,driverName,driverPhone,pDName,pDLocation,pDDestination,pDStartingTime,pDStartingDate,
            pDExpectedPrice;
    ProgressDialog pd;
    String ratingValueString,countRatingString,additionRatingString;
    ImageView imageViewTime;
    ImageView back,callDriver,callCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_history_running_details);

        currentTime=String.valueOf(System.currentTimeMillis());
        timestamp=currentTime;
        isAId=currentTime;

        rHCustomerName=findViewById(R.id.rHCustomerName);
        rHCustomerPhone=findViewById(R.id.rHCustomerPhone);
        rHCustomerDp=findViewById(R.id.rHCustomerDp);

        back=findViewById(R.id.backHRD);
        callCustomer=findViewById(R.id.callCustomer);
        callDriver=findViewById(R.id.callDriver);

        rHDriverName=findViewById(R.id.rHDriverName);
        rHDriverPhone=findViewById(R.id.rHDriverPhone);
        rHDriverDp=findViewById(R.id.rHDriverDp);

        imageViewTime=findViewById(R.id.imageViewTime);

        rHPostPerssonName=findViewById(R.id.rHPostPerssonName);
        rHPostLocation=findViewById(R.id.rHPostLocation);
        rHPostDestination=findViewById(R.id.rHPostDestination);
        rHPostStartingTime=findViewById(R.id.rHPostStartingTime);
        rHPostStartingDate=findViewById(R.id.rHPostStartingDate);
        rHExpectedPrice=findViewById(R.id.rHExpectedPrice);

        rHFinishJob=findViewById(R.id.rHFinishJob);
        cancelTrip=findViewById(R.id.cancelTrip);


        Intent intent=getIntent();
        rHId=intent.getStringExtra("rHId");


        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("ট্রিপ বাদ");
                builder.setMessage("আপনি কি ট্রিপ টি বাদ দিতে চান?");
                builder.setPositiveButton("বাদ দিন", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("RunningHistory").child(rHId);
                        ref.removeValue();

                        Intent intent=new Intent(SeeHistoryRunningDetailsActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
                builder.setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create().show();
            }

        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        callCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                String number=rHCustomerPhone.getText().toString().trim();

                intent.setData(Uri.parse("tel: "+number));
                startActivity(intent);
            }
        });


        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                String number=rHDriverPhone.getText().toString().trim();

                intent.setData(Uri.parse("tel: "+number));
                startActivity(intent);
            }
        });













        //loadRunningHistoryDetails
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("RunningHistory");
        Query query=ref.orderByChild("timestamp").equalTo(rHId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    customerName=""+ds.child("customerName").getValue();
                    customerPhone=""+ds.child("customerPhone").getValue();
                    driverName=""+ds.child("driverName").getValue();
                    driverPhone=""+ds.child("driverPhone").getValue();
                    customerDp=""+ds.child("customerDp").getValue(String.class);
                    driverDp=""+ds.child("driverDp").getValue(String.class);
                    pDName=""+ds.child("pDName").getValue();
                    pDLocation=""+ds.child("pDLocation").getValue();
                    pDDestination=""+ds.child("pDDestination").getValue();
                    pDStartingTime =""+ds.child("pDStartingTime").getValue();
                    pDStartingDate=""+ds.child("pDStartingDate").getValue();
                    pDExpectedPrice=""+ds.child("pDExpectedPrice").getValue();
                    customeruid=""+ds.child("customeruid").getValue();
                    driverPostId=""+ds.child("driverPostId").getValue();
                    driveruid=""+ds.child("driveruid").getValue();
                    isAccepted=""+ds.child("isAccepted").getValue();
                    pTime=""+ds.child("pTime").getValue();

                    rHCustomerName.setText(customerName);
                    rHCustomerPhone.setText(customerPhone);
                    rHDriverName.setText(driverName);
                    rHDriverPhone.setText(driverPhone);
                    rHPostLocation.setText(pDLocation);
                    rHPostDestination.setText(pDDestination);
                    rHPostStartingTime.setText(pDStartingTime);
                    rHPostStartingDate.setText(pDStartingDate);
                    rHPostPerssonName.setText(pDName);
                    rHExpectedPrice.setText(pDExpectedPrice);


                    if(pDStartingTime.equals("") || pDStartingDate.equals("")){
                        imageViewTime.setVisibility(View.GONE);
                    }

                    try{
                        Picasso.get().load(customerDp).placeholder(R.drawable.avatar).into(rHCustomerDp);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.avatar).into(rHCustomerDp);
                    }

                    try{
                        Picasso.get().load(driverDp).placeholder(R.drawable.avatar).into(rHDriverDp);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.avatar).into(rHDriverDp);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rHFinishJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //loadTheRating
                FirebaseDatabase.getInstance().getReference("Rating").child(driveruid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.getChildrenCount()>0){

                            Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                            if(map.get("value")!=null){
                                ratingValueString=map.get("value").toString();

                            }

                            if(map.get("count")!=null){
                                countRatingString=map.get("count").toString();

                            }

                            if(map.get("addition")!=null){
                                additionRatingString=map.get("addition").toString();

                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Star Rating
                Dialog dialog=new Dialog(SeeHistoryRunningDetailsActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_star_rating);
                dialog.show();

                RatingBar ratingBar=dialog.findViewById(R.id.dialog_rating_bar);
                TextView ratingNumber=dialog.findViewById(R.id.dialog_tv_rating);
                TextView submitRating=dialog.findViewById(R.id.submit_rating);
                EditText aboutDriver=dialog.findViewById(R.id.aboutDriver);



                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        ratingNumber.setText(String.format("%s",v));
                    }
                });

                submitRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pd=new ProgressDialog(SeeHistoryRunningDetailsActivity.this);
                        pd.setMessage("Adding");

                        String ratingString=String.valueOf(ratingBar.getRating());
                        float countFloat=Float.valueOf(countRatingString);
                        float additionFloat=Float.valueOf(additionRatingString);



                        float addOfCount= (float) (countFloat+1.00);
                        String stringAddOfCount=String.valueOf(addOfCount);


                        float ratingValue = Float.parseFloat(ratingValueString);
                        float rating=Float.parseFloat(ratingString);
                        float addOfValue=ratingValue+rating;
                        float addOfAddition=additionFloat+rating;
                        float division=addOfAddition/addOfCount;
                        String stringAddOfValue=String.valueOf(division);



                        DatabaseReference refRating=FirebaseDatabase.getInstance().getReference("Rating");

                        HashMap<String,Object> hashMap=new HashMap<>();

                        hashMap.put("value",stringAddOfValue);
                        hashMap.put("count",stringAddOfCount);
                        hashMap.put("addition",addOfAddition);

                        refRating.child(driveruid).setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        pd.dismiss();
                                        Toast.makeText(SeeHistoryRunningDetailsActivity.this, "Rated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(SeeHistoryRunningDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                        String timeStamp=String.valueOf(System.currentTimeMillis());
                        String AboutDriver=aboutDriver.getText().toString().trim();


                        DatabaseReference refAboutDriver=FirebaseDatabase.getInstance().getReference("AboutDrivers");

                        HashMap<String,Object> hashMapAboutDriver=new HashMap<>();

                        hashMapAboutDriver.put("comment",AboutDriver);
                        hashMapAboutDriver.put("cTime",timeStamp);
                        hashMapAboutDriver.put("uid",driveruid);
                        hashMapAboutDriver.put("dp",customerDp);
                        hashMapAboutDriver.put("name",customerName);

                        refAboutDriver.child(driveruid).child(timeStamp).setValue(hashMapAboutDriver)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        pd.dismiss();
                                        Toast.makeText(SeeHistoryRunningDetailsActivity.this, "Rated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(SeeHistoryRunningDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });





                        pd=new ProgressDialog(SeeHistoryRunningDetailsActivity.this);
                        pd.setMessage("Completing");
                        pd.show();

                        DatabaseReference refFinish=FirebaseDatabase.getInstance().getReference("History");

                        HashMap<String,Object> hashMapComplete=new HashMap<>();

                        hashMapComplete.put("isAId",timestamp);
                        hashMapComplete.put("driverPostId",driverPostId);
                        hashMapComplete.put("isAccepted",isAccepted);
                        hashMapComplete.put("timestamp",timestamp);
                        hashMapComplete.put("customeruid",customeruid);
                        hashMapComplete.put("customerPhone",customerPhone);
                        hashMapComplete.put("customerDp",customerDp);
                        hashMapComplete.put("customerName",customerName);


                        hashMapComplete.put("pDName",pDName);
                        hashMapComplete.put("pDLocation",pDLocation);
                        hashMapComplete.put("pDDestination",pDDestination);
                        hashMapComplete.put("pDExpectedPrice",pDExpectedPrice);
                        hashMapComplete.put("pDStartingTime",pDStartingTime);
                        hashMapComplete.put("pDStartingDate",pDStartingDate);

                        hashMapComplete.put("driverDp",driverDp);
                        hashMapComplete.put("driveruid",driveruid);
                        hashMapComplete.put("driverName",driverName);
                        hashMapComplete.put("driverPhone",driverPhone);

                        hashMapComplete.put("pTime",pTime);


                        refFinish.child(timestamp).setValue(hashMapComplete)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        pd.dismiss();
                                        Toast.makeText(SeeHistoryRunningDetailsActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(SeeHistoryRunningDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        removePost();


                    }
                });






            }
        });
    }

    private void saveToHistory() {




    }

    private void removePost(){
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("RunningHistory").child(rHId);
        ref.removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent=new Intent(SeeHistoryRunningDetailsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}