package com.example.projectmove.Activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.projectmove.Model.NotificationData;

import com.example.projectmove.Model.NotificationData;
import com.example.projectmove.Model.PushNotification;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterComments;
import com.example.projectmove.Utilis.Adapter.AdapterCommentsForDriver;
import com.example.projectmove.Utilis.Class.Comments;
import com.example.projectmove.api.ApiUtilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class DriverPostDetailsActivity extends AppCompatActivity {


    CircleImageView uCPictureIV,cAvatarIV;
    TextView uCNameTV,pCTimeTV,pCustomerCost,pCustomerName,pCustomerLocation,pCustomerDestination,pCustomerStartingTime,
            pCustomerStartingDate,pCustomerVehicleType,pCustomerCommentsTV;

    String postId,customerPhone,CDp,myUid,myPhone,myDp,myName;

    TextView detailsCommentET;

    Spinner firstSpinner,secondSpinner;

    ProgressDialog pd;

    RecyclerView recycleviewCustomerPostDetails;

    List<Comments> commentList;
    AdapterCommentsForDriver adapterCommentsForDriver;

    TextView seeTimer;

    long timeLeft;

    ExtendedFloatingActionButton acceptAs;

    TextView isAcceptedTV;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference vehicleModelRef;
    ValueEventListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_details);

        uCPictureIV=findViewById(R.id.uCDetailsPictureIV);
        uCNameTV=findViewById(R.id.uCDetailsNameTV);
        pCTimeTV=findViewById(R.id.pCDetailsTimeTV);
        pCustomerCost=findViewById(R.id.pDetailsCustomerCost);
        pCustomerName=findViewById(R.id.pDetailsCustomerName);
        pCustomerLocation=findViewById(R.id.pDetailsCustomerLocation);
        pCustomerDestination=findViewById(R.id.pDetailsCustomerDestination);
        pCustomerStartingTime=findViewById(R.id.pDetailsCustomerStartingTime);
        pCustomerStartingDate=findViewById(R.id.pDetailsCustomerStartingDate);
        pCustomerVehicleType=findViewById(R.id.pDetailsCustomerVehicleType);
        pCustomerCommentsTV=findViewById(R.id.pDetailsCustomerCommentsTV);

        cAvatarIV=findViewById(R.id.cAvatarIVDriverPost);

        acceptAs=findViewById(R.id.acceptAs);

        seeTimer=findViewById(R.id.seeTimer);

        recycleviewCustomerPostDetails=findViewById(R.id.recycleviewCustomerPostDetails);

        detailsCommentET=findViewById(R.id.detailsCommentET);

        isAcceptedTV=findViewById(R.id.isAccepted);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        vehicleModelRef=FirebaseDatabase.getInstance().getReference("VehicleModel");


        Intent intent=getIntent();
        postId=intent.getStringExtra("postId");
        customerPhone=intent.getStringExtra("customerPhone");

        checkUserStatus();
        loadUserInfo();
        loadComments();
        seeTimerMethod();


        //loadPost
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");
        Query query=ref.orderByChild("cPId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String pCVehicleType=""+ds.child("pCVehicleType").getValue();
                    String pCStartingDate =""+ds.child("pCStartingDate").getValue();
                    String pCStartingTime=""+ds.child("pCStartingTime").getValue();
                    String pCPrice=""+ds.child("pCPrice").getValue();
                    String pCLocation=""+ds.child("pCLocation").getValue();
                    String pCDestination=""+ds.child("pCDestination").getValue();
                    CDp=""+ds.child("CDp").getValue(String.class);
                    String pCName=""+ds.child("pCName").getValue();
                    String CUserName=""+ds.child("CUserName").getValue();
                    String CPTime=""+ds.child("CPTime").getValue();
                    String commentCount=""+ds.child("pComments").getValue();
                    String isAccepted=""+ds.child("isAccepted").getValue();

                    String timer = "" + ds.child("timer").getValue(long.class);
                    long timeInMillis = Long.parseLong(timer);

                    if(timeInMillis<=System.currentTimeMillis()){
                        detailsCommentET.setVisibility(View.GONE);
                    }
                    else {
                        detailsCommentET.setVisibility(View.VISIBLE);
                    }

                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(CPTime));
                    String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();




                    uCNameTV.setText(CUserName);
                    pCTimeTV.setText(pTime);
                    pCustomerCost.setText(pCPrice);
                    pCustomerName.setText(pCName);
                    pCustomerLocation.setText(pCLocation);
                    pCustomerDestination.setText(pCDestination);
                    pCustomerStartingTime.setText(pCStartingTime);
                    pCustomerStartingDate.setText(pCStartingDate);
                    pCustomerVehicleType.setText(pCVehicleType);
                    pCustomerCommentsTV.setText(commentCount+" কমেন্ট");
                    isAcceptedTV.setText(isAccepted);



                    try{
                        Picasso.get().load(CDp).placeholder(R.drawable.avatar).into(uCPictureIV);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.avatar).into(uCPictureIV);
                    }

                    try{
                        Picasso.get().load(myDp).placeholder(R.drawable.avatar).into(cAvatarIV);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.avatar).into(cAvatarIV);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        detailsCommentET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForComment();
            }
        });

        acceptAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addAcceptance();
            }
        });




    }

    private void addAcceptance() {

    }

    private void seeTimerMethod() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");
        Query query=ref.orderByChild("cPId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    String timer = "" + ds.child("timer").getValue(long.class);
                    long timeInMillis = Long.parseLong(timer);
                    timeLeft = timeInMillis - System.currentTimeMillis();
                    String timeLeftString = String.format("%d মিনিট বাকি", TimeUnit.MILLISECONDS.toMinutes(timeLeft));

                    if(timeInMillis<=System.currentTimeMillis()){
                        seeTimer.setText("সময় শেষ");
                    }
                    else {
                        seeTimer.setText(timeLeftString);
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

        recycleviewCustomerPostDetails.setLayoutManager(layoutManager);

        commentList=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Comments comments=ds.getValue(Comments.class);

                    commentList.add(comments);

                    adapterCommentsForDriver=new AdapterCommentsForDriver(getApplicationContext(),commentList,myUid,postId,myPhone,myName,myDp);

                    recycleviewCustomerPostDetails.setAdapter(adapterCommentsForDriver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialogForComment() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_add_comment_bottom_sheetlayout);

        Spinner spinnerVehicleModel=dialog.findViewById(R.id.spinnerVehicleModel);

        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinnerVehicleModel.setAdapter(adapter);



        fetchVehicleModelData();

        TextView commentWithMoney=dialog.findViewById(R.id.commentWithMoney);

        TextView yesButton=dialog.findViewById(R.id.yesButton);
        TextView noButton=dialog.findViewById(R.id.noButton);

        firstSpinner = dialog.findViewById(R.id.first_spinner);
        secondSpinner = dialog.findViewById(R.id.second_spinner);

        ImageView cancel=dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        // Set integer values as options for the Spinners
        List<Integer> thousandValues = new ArrayList<>();
        for (int i = 1000; i <= 10000; i=i+1000) {
            thousandValues.add(i);
        }

        // Set integer values as options for the Spinners
        List<Integer> hundredValues = new ArrayList<>();
        for (int i = 100; i <= 1000; i=i+100) {
            hundredValues.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thousandValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hundredValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        firstSpinner.setAdapter(adapter);
        secondSpinner.setAdapter(adapter2);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(DriverPostDetailsActivity.this);
                pd.setMessage("Adding Comment");



                String comment=yesButton.getText().toString()+" যাব";


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myName, "কমেন্ট করেছেনঃ "+comment), "/topics/"+postId+"postCustomer");
                sendNotification(pushNotification);

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(DriverPostDetailsActivity.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+postId+"postCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //addUserIdForNotificationFragment
                        String subscribedTopic=postId;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("cId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myUid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",myDp);
                hashMap.put("uName",myName);
                hashMap.put("commentSub","");
                hashMap.put("postId",postId);
                hashMap.put("pReplies","0");


                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(DriverPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(DriverPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });







                // Retrieve a list of user IDs subscribed to the topic
                String subscribedTopic=postId;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("Topics").child(subscribedTopic).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> userIds = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.getKey();
                            userIds.add(userId);

                        }

                        for (String userId : userIds) {
                            // TODO: Use the user ID as needed
                            String timestamp=String.valueOf(System.currentTimeMillis());
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userId).child(timestamp);


                            HashMap<String,Object> hashMap=new HashMap<>();


                            hashMap.put("timeStamp",timestamp);
                            hashMap.put("nId",timestamp);
                            hashMap.put("message","কমেন্ট করেছেনঃ "+comment);
                            hashMap.put("postId",postId);
                            hashMap.put("uName",myName);
                            hashMap.put("uDp",myDp);
                            hashMap.put("userId",userId);
                            hashMap.put("uPhone",myPhone);


                            ref.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DriverPostDetailsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DriverPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });











            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(DriverPostDetailsActivity.this);
                pd.setMessage("Adding Comment");

                String comment=noButton.getText().toString()+" যাব না";


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myName, "কমেন্ট করেছেনঃ "+comment), "/topics/"+postId+"postCustomer");
                sendNotification(pushNotification);

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(DriverPostDetailsActivity.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+postId+"postCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //addUserIdForNotificationFragment
                        String subscribedTopic=postId;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("cId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myUid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",myDp);
                hashMap.put("uName",myName);
                hashMap.put("commentSub","");
                hashMap.put("postId",postId);
                hashMap.put("pReplies","0");

                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(DriverPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(DriverPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });





                // Retrieve a list of user IDs subscribed to the topic
                String subscribedTopic=postId;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("Topics").child(subscribedTopic).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> userIds = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.getKey();
                            userIds.add(userId);

                        }

                        for (String userId : userIds) {
                            // TODO: Use the user ID as needed
                            String timestamp=String.valueOf(System.currentTimeMillis());
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userId).child(timestamp);


                            HashMap<String,Object> hashMap=new HashMap<>();


                            hashMap.put("timeStamp",timestamp);
                            hashMap.put("nId",timestamp);
                            hashMap.put("message","কমেন্ট করেছেনঃ "+comment);
                            hashMap.put("postId",postId);
                            hashMap.put("uName",myName);
                            hashMap.put("uDp",myDp);
                            hashMap.put("userId",userId);
                            hashMap.put("uPhone",myPhone);


                            ref.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DriverPostDetailsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DriverPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });









            }
        });

        commentWithMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int firstValue = (int) firstSpinner.getSelectedItem();
                int secondValue = (int) secondSpinner.getSelectedItem();
                int sum = firstValue + secondValue;
                String vehicleModelSub= (String) spinnerVehicleModel.getSelectedItem();

                pd=new ProgressDialog(DriverPostDetailsActivity.this);
                pd.setMessage("Adding Comment");

                String commentSub=String.valueOf(sum);
                String comment=commentSub+" টাকায় চলেন";
                String vehicleModel="গাড়ির মডেল: "+vehicleModelSub;

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(DriverPostDetailsActivity.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }


                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+postId+"postCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //addUserIdForNotificationFragment
                        String subscribedTopic=postId;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"acceptCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp+"Accept";
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myName, "কমেন্ট করেছেনঃ "+comment), "/topics/"+postId+"postCustomer");
                sendNotification(pushNotification);

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("cId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myUid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",myDp);
                hashMap.put("uName",myName);
                hashMap.put("commentSub",commentSub);
                hashMap.put("vehicleModel",vehicleModel);
                hashMap.put("postId",postId);
                hashMap.put("pReplies","0");

                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(DriverPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(DriverPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });










                // Retrieve a list of user IDs subscribed to the topic
                String subscribedTopic=postId;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child("Topics").child(subscribedTopic).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> userIds = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userId = snapshot.getKey();
                            userIds.add(userId);

                        }

                        for (String userId : userIds) {
                            // TODO: Use the user ID as needed
                            String timestamp=String.valueOf(System.currentTimeMillis());
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userId).child(timestamp);


                            HashMap<String,Object> hashMap=new HashMap<>();


                            hashMap.put("timeStamp",timestamp);
                            hashMap.put("nId",timestamp);
                            hashMap.put("message","কমেন্ট করেছেনঃ "+comment);
                            hashMap.put("postId",postId);
                            hashMap.put("uName",myName);
                            hashMap.put("uDp",myDp);
                            hashMap.put("userId",userId);
                            hashMap.put("uPhone",myPhone);


                            ref.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DriverPostDetailsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DriverPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });












            }
        });



        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void sendNotification(PushNotification pushNotification) {
        ApiUtilities.getClient().sendNotification(pushNotification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, retrofit2.Response<PushNotification> response) {
                if(response.isSuccessful()){
                    Toast.makeText(DriverPostDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(DriverPostDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(DriverPostDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchVehicleModelData() {
        listener=vehicleModelRef.child(user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot myData:snapshot.getChildren())
                    list.add(myData.getValue().toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    boolean mProcessComment=false;


    private void updateCommentCount() {
        mProcessComment=true;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessComment){
                    String comments=""+snapshot.child("pComments").getValue();
                    int newCommentVal=Integer.parseInt(comments)+1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    mProcessComment=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadUserInfo() {
        Query myRef=FirebaseDatabase.getInstance().getReference("Users");
        myRef.orderByChild("uid").equalTo(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    myName=""+ds.child("username").getValue();
                    myDp=""+ds.child("imageUrl").getValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserStatus(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            myPhone=user.getPhoneNumber();
            myUid=user.getUid();

        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}