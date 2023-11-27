package com.example.projectmove.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Model.NotificationData;
import com.example.projectmove.Model.PushNotification;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterComments;
import com.example.projectmove.Utilis.Adapter.AdapterDriversJobs;
import com.example.projectmove.Utilis.Class.Comments;
import com.example.projectmove.Utilis.Class.DriversJobs;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class CustomerPostDetailsActivity extends AppCompatActivity {


    String hisUid, myUid,myPhone,myName,myDp,
    postId,hisDp,hisName,timestamp,driverPhone;

    private String isAcceptedStatus;


    ProgressDialog pd;

    CircleImageView uPictureIV;
    TextView uNameTV,pTimeTV,pDNameTV,pDLocationTV,pDDestinationTV,pDExpectedPriceTV,pDStartingTimeTV,pDStartingDateTV,pCommentsTV
            ,pDriverVehicleType,pDriverNumberPlate;
    ImageButton moreBtn;
    LinearLayout profileLayout;

    Button acceptBtn;

    RecyclerView recyclerView;

    List<Comments> commentList;
    AdapterComments adapterComments;

    TextView commentET;
    ImageButton sendBtn;
    ImageView cAvatarIV;


    ExtendedFloatingActionButton acceptAs;

    FirebaseAuth auth;
    String uid,asCustomerPhoneNumber;
    FirebaseUser user;

    TextView isAccepted;

    String didPostDp;

    RadioGroup radioGroup;
    RadioButton radioButton;
    int radioId;

    Spinner firstSpinner;
    Spinner secondSpinner,spinnerVehicleModel;

    private EditText addBidAmountET;

    ValueEventListener listener;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    DatabaseReference vehicleModelRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_post_details);


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();




        Intent intent=getIntent();
        postId=intent.getStringExtra("postId");
        driverPhone=intent.getStringExtra("driverPhone");

        uPictureIV=findViewById(R.id.uPictureIV);
        uNameTV=findViewById(R.id.uNameTV);
        pTimeTV=findViewById(R.id.pTimeTV);
        pDNameTV=findViewById(R.id.pDriverName);
        pDLocationTV=findViewById(R.id.pDriverLocation);
        pDDestinationTV=findViewById(R.id.pDriverDestination);
        pDExpectedPriceTV=findViewById(R.id.pDriverExpectedCost);
        pDStartingTimeTV=findViewById(R.id.pDriverStartingTime);
        pDStartingDateTV=findViewById(R.id.pDriverStartingDate);
        pCommentsTV=findViewById(R.id.pCommentsTV);
        pDriverVehicleType=findViewById(R.id.pDriverVehicleType);
        pDriverNumberPlate=findViewById(R.id.pDriverNumberPlate);


        moreBtn=findViewById(R.id.moreButton);
        profileLayout=findViewById(R.id.profileLayout);

        commentET=findViewById(R.id.commentET);
        sendBtn=findViewById(R.id.sendBtn);
        cAvatarIV=findViewById(R.id.cAvatarIV);

        recyclerView=findViewById(R.id.recycleviewPostDetails);

        acceptAs=findViewById(R.id.acceptAs);

        isAccepted=findViewById(R.id.isAccepted);

        timestamp=String.valueOf(System.currentTimeMillis());

        vehicleModelRef=FirebaseDatabase.getInstance().getReference("VehicleModel");




        acceptAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });

        commentET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForComment();
            }
        });







        //loadPostInfo()
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");
        Query query=ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String pDName=""+ds.child("pDName").getValue();
                    String pDLocation =""+ds.child("pDLocation").getValue();
                    String pDDestination=""+ds.child("pDDestination").getValue();
                    String pDExpectedPrice=""+ds.child("pDExpectedPrice").getValue();
                    String pDStartingTime=""+ds.child("pDStartingTime").getValue();
                    String pDStartingDate=""+ds.child("pDStartingDate").getValue();
                    hisUid=""+ds.child("uid").getValue(String.class);
                    String uPhone=""+ds.child("uPhone").getValue();
                    hisDp=""+ds.child("uDp").getValue(String.class);
                    hisName=""+ds.child("uName").getValue(String.class);
                    String pTimeStamp=""+ds.child("pTime").getValue();
                    String commentCount=""+ds.child("pComments").getValue();
                    String pDVehicleType=""+ds.child("pDVehicleType").getValue();
                    String pDNumberPlate=""+ds.child("pDNumberPlate").getValue();

                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

                    pDNameTV.setText(pDName);
                    pDLocationTV.setText(pDLocation);
                    pDDestinationTV.setText(pDDestination);
                    pDExpectedPriceTV.setText(pDExpectedPrice);
                    pDStartingTimeTV.setText(pDStartingTime);
                    pDStartingDateTV.setText(pDStartingDate);
                    pTimeTV.setText(pTime);
                    pCommentsTV.setText(commentCount+" কমেন্ট");
                    pDriverVehicleType.setText(pDVehicleType);
                    pDriverNumberPlate.setText(pDNumberPlate);
                    acceptAs.setText("("+pDExpectedPrice+") টাকায় ট্রিপ গ্রহন করুন");


                    uNameTV.setText(hisName);

                    try{
                        Picasso.get().load(hisDp).placeholder(R.drawable.avatar).into(uPictureIV);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.avatar).into(uPictureIV);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        checkUserStatus();

        loadUserInfo();

        loadComments();

        isAccepted();

        hideAcceptButton();



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });



    }

    private void hideAcceptButton() {

        DatabaseReference refHide= FirebaseDatabase.getInstance().getReference("DriversPosts");
        Query queryHide=refHide.orderByChild("pId").equalTo(postId);
        queryHide.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){

                    String hisUid=""+ds.child("uid").getValue(String.class);

                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        String myUid=user.getUid();

                        if(hisUid.equals(myUid)){
                            acceptAs.setVisibility(View.GONE);
                        }
                        else{
                            acceptAs.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        startActivity(new Intent(CustomerPostDetailsActivity.this,MainActivity.class));
                        finish();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void checkButton(View v) {
        radioId = radioGroup.getCheckedRadioButtonId();

    }

    private void showDialogForComment() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_add_comment_bottom_sheetlayout);

        TextView driverSectionTV=dialog.findViewById(R.id.driverSectionTV);
        View driverSectionView=dialog.findViewById(R.id.driverSectionView);

        spinnerVehicleModel=dialog.findViewById(R.id.spinnerVehicleModel);
        spinnerVehicleModel.setVisibility(View.GONE);
        driverSectionTV.setVisibility(View.GONE);
        driverSectionView.setVisibility(View.GONE);

        list=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinnerVehicleModel.setAdapter(adapter);



        fetchVehicleModelData();

        TextView commentWithMoney=dialog.findViewById(R.id.commentWithMoney);
        commentWithMoney.setText("টাকা নিয়ে কমেন্ট করুন");

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
        for (int i = 0000; i <= 100000; i=i+1000) {
            thousandValues.add(i);
        }

        // Set integer values as options for the Spinners
        List<Integer> hundredValues = new ArrayList<>();
        for (int i = 000; i <= 1000; i=i+100) {
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
                pd=new ProgressDialog(CustomerPostDetailsActivity.this);
                pd.setMessage("Adding Comment");

                String comment=yesButton.getText().toString()+" যাব";

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(CustomerPostDetailsActivity.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+postId+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=postId;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"postCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myName, "কমেন্ট করেছেনঃ "+comment), "/topics/"+postId+"postDriver");
                sendNotification(pushNotification);


                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId).child("Comments");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("cId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myUid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",myDp);
                hashMap.put("uName",myName);
                hashMap.put("postId",postId);
                hashMap.put("pReplies","0");
                hashMap.put("commentSub","");
                hashMap.put("pReplies","0");


                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(CustomerPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CustomerPostDetailsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                pd=new ProgressDialog(CustomerPostDetailsActivity.this);
                pd.setMessage("Adding Comment");

                String comment=noButton.getText().toString()+" যাব না";

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(CustomerPostDetailsActivity.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+postId+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=postId;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"postCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myName, "কমেন্ট করেছেনঃ "+comment), "/topics/"+postId+"postDriver");
                sendNotification(pushNotification);

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId).child("Comments");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("cId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myUid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",myDp);
                hashMap.put("uName",myName);
                hashMap.put("postId",postId);
                hashMap.put("pReplies","0");
                hashMap.put("commentSub","");
                hashMap.put("pReplies","0");


                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(CustomerPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CustomerPostDetailsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

                pd=new ProgressDialog(CustomerPostDetailsActivity.this);
                pd.setMessage("Adding Comment");

                String commentSub=String.valueOf(sum);
                String comment=commentSub+" টাকায় চলেন";

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(CustomerPostDetailsActivity.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+postId+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=postId;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"postCustomer").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+timestamp+"acceptDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String subscribedTopic=timestamp+"Accept";
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myName, "কমেন্ট করেছেনঃ "+comment), "/topics/"+postId+"postDriver");
                sendNotification(pushNotification);


                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId).child("Comments");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("cId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myUid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",myDp);
                hashMap.put("uName",myName);
                hashMap.put("postId",postId);
                hashMap.put("pReplies","0");
                hashMap.put("commentSub",commentSub);
                hashMap.put("pReplies","0");


                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(CustomerPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CustomerPostDetailsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CustomerPostDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(CustomerPostDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(CustomerPostDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_accept_bottom_sheetlayout);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        ImageView shareLayout = dialog.findViewById(R.id.layoutShare);

        ImageView cancel=dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                addAcceptance();
                AddAcceptanceId();
                removePost();
                commentET.setVisibility(View.GONE);

            }
        });

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBidAmountET=dialog.findViewById(R.id.addBidAmountET);

                dialog.dismiss();
                addAcceptanceWithBid();
                AddAcceptanceId();
                removePost();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void addAcceptanceWithBid() {

        pd=new ProgressDialog(this);
        pd.setMessage("Accepting");

        uid=user.getUid();
        asCustomerPhoneNumber=user.getPhoneNumber();


        String isAccepted="Accepted";
        String pDName=pDNameTV.getText().toString().trim();
        String pDLocation=pDLocationTV.getText().toString().trim();
        String pDDestination=pDDestinationTV.getText().toString().trim();
        String pDExpectedPrice=addBidAmountET.getText().toString().trim();
        String pDStartingTime=pDStartingTimeTV.getText().toString().trim();
        String pDStartingDate=pDStartingDateTV.getText().toString().trim();

        didPostDp=hisDp;
        String didPostuid=hisUid;
        String didPostName=hisName;

        String postTime=pTimeTV.getText().toString().trim();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("RunningHistory");


        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("isAId",timestamp);
        hashMap.put("driverPostId",postId);
        hashMap.put("isAccepted",isAccepted);
        hashMap.put("timestamp",timestamp);
        hashMap.put("customeruid",myUid);
        hashMap.put("customerPhone",myPhone);
        hashMap.put("customerDp",myDp);
        hashMap.put("customerName",myName);


        hashMap.put("pDName",pDName);
        hashMap.put("pDLocation",pDLocation);
        hashMap.put("pDDestination",pDDestination);
        hashMap.put("pDExpectedPrice",pDExpectedPrice);
        hashMap.put("pDStartingTime",pDStartingTime);
        hashMap.put("pDStartingDate",pDStartingDate);

        hashMap.put("driverDp",didPostDp);
        hashMap.put("driveruid",didPostuid);
        hashMap.put("driverName",didPostName);
        hashMap.put("driverPhone",driverPhone);

        hashMap.put("pTime",postTime);


        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(CustomerPostDetailsActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removePost() {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId);
        ref.removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent=new Intent(CustomerPostDetailsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddAcceptanceId() {

        DatabaseReference refaddAcceptanceId=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId);

        String isAccepted="Accepted";

        HashMap<String,Object> hashMapAddAcceptanceID=new HashMap<>();
        hashMapAddAcceptanceID.put("isAId",timestamp);
        hashMapAddAcceptanceID.put("isAccepted",isAccepted);

        refaddAcceptanceId.updateChildren(hashMapAddAcceptanceID);
    }

    private void isAccepted() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");
        Query query=ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    isAcceptedStatus=""+ds.child("isAccepted").getValue();
                    isAccepted.setText(isAcceptedStatus);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addAcceptance() {

        pd=new ProgressDialog(this);
        pd.setMessage("Accepting");

        uid=user.getUid();
        asCustomerPhoneNumber=user.getPhoneNumber();


        String isAccepted="Accepted";
        String pDName=pDNameTV.getText().toString().trim();
        String pDLocation=pDLocationTV.getText().toString().trim();
        String pDDestination=pDDestinationTV.getText().toString().trim();
        String pDExpectedPrice=pDExpectedPriceTV.getText().toString().trim();
        String pDStartingTime=pDStartingTimeTV.getText().toString().trim();
        String pDStartingDate=pDStartingDateTV.getText().toString().trim();

        didPostDp=hisDp;
        String didPostuid=hisUid;
        String didPostName=hisName;

        String postTime=pTimeTV.getText().toString().trim();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("RunningHistory");


        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("isAId",timestamp);
        hashMap.put("driverPostId",postId);
        hashMap.put("isAccepted",isAccepted);
        hashMap.put("timestamp",timestamp);
        hashMap.put("customeruid",myUid);
        hashMap.put("customerPhone",myPhone);
        hashMap.put("customerDp",myDp);
        hashMap.put("customerName",myName);


        hashMap.put("pDName",pDName);
        hashMap.put("pDLocation",pDLocation);
        hashMap.put("pDDestination",pDDestination);
        hashMap.put("pDExpectedPrice",pDExpectedPrice);
        hashMap.put("pDStartingTime",pDStartingTime);
        hashMap.put("pDStartingDate",pDStartingDate);

        hashMap.put("driverDp",didPostDp);
        hashMap.put("driveruid",didPostuid);
        hashMap.put("driverName",didPostName);
        hashMap.put("driverPhone",driverPhone);

        hashMap.put("pTime",postTime);


        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(CustomerPostDetailsActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });







    }

    private void loadComments() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        commentList=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId).child("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Comments comments=ds.getValue(Comments.class);

                    commentList.add(comments);

                    adapterComments=new AdapterComments(getApplicationContext(),commentList,myUid,postId);

                    recyclerView.setAdapter(adapterComments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void postComment() {


        String comment=commentET.getText().toString().trim();

        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this, "Enter a Comment", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp=String.valueOf(System.currentTimeMillis());
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId).child("Comments");


        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("cId",timestamp);
        hashMap.put("comment",comment);
        hashMap.put("timestamp",timestamp);
        hashMap.put("uid",myUid);
        hashMap.put("uPhone",myPhone);
        hashMap.put("uDp",myDp);
        hashMap.put("uName",myName);

        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(CustomerPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                        commentET.setText("");
                        updateCommentCount();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(CustomerPostDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    boolean mProcessComment=false;


    private void updateCommentCount() {
        mProcessComment=true;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId);
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
    }

    private void loadPostsInfo() {


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

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}