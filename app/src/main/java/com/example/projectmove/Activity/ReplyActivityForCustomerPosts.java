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
import com.example.projectmove.Model.PushNotification;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterComments;
import com.example.projectmove.Utilis.Adapter.AdapterCommentsForDriver;
import com.example.projectmove.Utilis.Adapter.AdapterCustomersJobs;
import com.example.projectmove.Utilis.Adapter.AdapterRepliesForDriver;
import com.example.projectmove.Utilis.Class.Comments;
import com.example.projectmove.Utilis.Class.CustomersJobs;
import com.example.projectmove.Utilis.Class.Replies;
import com.example.projectmove.api.ApiUtilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ReplyActivityForCustomerPosts extends AppCompatActivity {

    String cid,uid,name,uPhone,image,comment,timestamp,commentSub,vehicleModel,postId;
    TextView userName,userComment,userTimeStamp,userVehicleModel,replyCount;
    CircleImageView userImage;
    TextView replyButton;
    Spinner spinnerVehicleModel,firstSpinner,secondSpinner;
    ProgressDialog pd;
    RecyclerView recyclerView;
    List<Replies> repliesList;
    AdapterRepliesForDriver adapterRepliesForDriver;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser user;
    String myPhone,myUserName,myAddress,myNationalIdCardNo,myuid,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_for_customer_posts);

        userName=findViewById(R.id.DnameReplyForCustomersPostsTV);
        userImage=findViewById(R.id.DavatarReplyForCustomersPostsIV);
        userComment=findViewById(R.id.DcommentReplyForCustomersPostsTV);
        userTimeStamp=findViewById(R.id.DtimeReplyForCustomersPostsTV);
        userVehicleModel=findViewById(R.id.DVehicleModelReplyForCustomersPostsTV);

        replyButton=findViewById(R.id.commentReplyForCustomersPostsET);
        recyclerView=findViewById(R.id.commentsReplyForCustomersPostsRecyclerView);
        replyCount=findViewById(R.id.DReplyCountReplyForCustomersPostsTV);

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");

        myPhone=user.getPhoneNumber();
        myuid=user.getUid();


        Intent intent=getIntent();
        cid=intent.getStringExtra("cid");
        uid=intent.getStringExtra("uid");
        name=intent.getStringExtra("name");
        uPhone=intent.getStringExtra("uPhone");
        image=intent.getStringExtra("image");
        comment=intent.getStringExtra("comment");
        timestamp=intent.getStringExtra("timestamp");
        commentSub=intent.getStringExtra("commentSub");
        vehicleModel=intent.getStringExtra("vehicleModel");
        postId=intent.getStringExtra("postId");



        try{
            Picasso.get().load(image).placeholder(R.drawable.avatar).into(userImage);
        }catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(userImage);
        }

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        userName.setText(name);
        userTimeStamp.setText(pTime);
        userComment.setText(comment);
        userVehicleModel.setText(vehicleModel);

        loadReplies();

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        //loadUserInfo

        DatabaseReference refForLoadUser= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments");
        Query query=refForLoadUser.orderByChild("cId").equalTo(cid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String pReplies=""+ds.child("pReplies").getValue();

                    replyCount.setText(pReplies+" রিপ্লাই");




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadUserInfo();

    }


    private void loadUserInfo() {

        myRef.child(myPhone).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myRef.child(myPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                    if(map.get("username")!=null){
                        myUserName=map.get("username").toString();

                    }

                    if(map.get("address")!=null){
                        myAddress=map.get("address").toString();

                    }

                    if(map.get("nationalIdCardNo")!=null){
                        myNationalIdCardNo=map.get("nationalIdCardNo").toString();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_add_comment_bottom_sheetlayout);

        TextView driverSectionTV=dialog.findViewById(R.id.driverSectionTV);
        View driverSectionView=dialog.findViewById(R.id.driverSectionView);
        TextView choosetxt=dialog.findViewById(R.id.choosetxt);
        Button commentWithMoney=dialog.findViewById(R.id.commentWithMoney);

        commentWithMoney.setText("টাকা নিয়ে কমেন্ট করুন");

        TextView yesButton=dialog.findViewById(R.id.yesButton);
        TextView noButton=dialog.findViewById(R.id.noButton);

        spinnerVehicleModel=dialog.findViewById(R.id.spinnerVehicleModel);
        spinnerVehicleModel.setVisibility(View.GONE);
        driverSectionTV.setVisibility(View.GONE);
        driverSectionView.setVisibility(View.GONE);
        choosetxt.setText("রিপ্লাই করুন");

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
                pd=new ProgressDialog(ReplyActivityForCustomerPosts.this);
                pd.setMessage("Adding Comment");

                String comment=yesButton.getText().toString()+" যাব";

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(ReplyActivityForCustomerPosts.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+cid+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //addUserIdForNotificationFragment
                        String subscribedTopic=cid;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myUserName, "রিপ্লাই করেছেনঃ "+comment), "/topics/"+cid+"postDriver");
                sendNotification(pushNotification);

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments")
                        .child(cid).child("Replies");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("rId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myuid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",value);
                hashMap.put("uName",myUserName);
                hashMap.put("postId",postId);
                hashMap.put("commentSub","");
                hashMap.put("cid",cid);


                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(ReplyActivityForCustomerPosts.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ReplyActivityForCustomerPosts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });





                // Retrieve a list of user IDs subscribed to the topic
                String subscribedTopic=cid;
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
                            hashMap.put("message","রিপ্লাই করেছেনঃ "+comment);
                            hashMap.put("postId",postId);
                            hashMap.put("uName",name);
                            hashMap.put("uDp",image);
                            hashMap.put("userId",userId);


                            ref.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ReplyActivityForCustomerPosts.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ReplyActivityForCustomerPosts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                pd=new ProgressDialog(ReplyActivityForCustomerPosts.this);
                pd.setMessage("Adding Comment");

                String comment=noButton.getText().toString()+" যাব না";

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(ReplyActivityForCustomerPosts.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+cid+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //addUserIdForNotificationFragment
                        String subscribedTopic=cid;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });


                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myUserName, "রিপ্লাই করেছেনঃ "+comment), "/topics/"+cid+"postDriver");
                sendNotification(pushNotification);

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments")
                        .child(cid).child("Replies");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("rId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myuid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",value);
                hashMap.put("uName",myUserName);
                hashMap.put("postId",postId);
                hashMap.put("commentSub","");
                hashMap.put("cid",cid);


                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(ReplyActivityForCustomerPosts.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ReplyActivityForCustomerPosts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });




                // Retrieve a list of user IDs subscribed to the topic
                String subscribedTopic=cid;
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
                            hashMap.put("message","রিপ্লাই করেছেনঃ "+comment);
                            hashMap.put("postId",postId);
                            hashMap.put("uName",name);
                            hashMap.put("uDp",image);
                            hashMap.put("userId",userId);


                            ref.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ReplyActivityForCustomerPosts.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ReplyActivityForCustomerPosts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

                pd=new ProgressDialog(ReplyActivityForCustomerPosts.this);
                pd.setMessage("Adding Comment");

                String commentSub=String.valueOf(sum);
                String comment=commentSub+" টাকায় চলেন";

                if(TextUtils.isEmpty(comment)){
                    Toast.makeText(ReplyActivityForCustomerPosts.this, "Enter a Comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                String timestamp=String.valueOf(System.currentTimeMillis());

                //subscribe
                FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+cid+"postDriver").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //addUserIdForNotificationFragment
                        String subscribedTopic=cid;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).child(sUserId).setValue(true);
                    }
                });

                //Notification
                PushNotification pushNotification = new PushNotification(new NotificationData(myUserName, "রিপ্লাই করেছেনঃ "+comment), "/topics/"+cid+"postDriver");
                sendNotification(pushNotification);


                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments")
                        .child(cid).child("Replies");


                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("rId",timestamp);
                hashMap.put("comment",comment);
                hashMap.put("timestamp",timestamp);
                hashMap.put("uid",myuid);
                hashMap.put("uPhone",myPhone);
                hashMap.put("uDp",value);
                hashMap.put("uName",myUserName);
                hashMap.put("postId",postId);
                hashMap.put("commentSub",commentSub);
                hashMap.put("cid",cid);

                ref.child(timestamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                pd.dismiss();
                                Toast.makeText(ReplyActivityForCustomerPosts.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                updateCommentCount();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ReplyActivityForCustomerPosts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });








                // Retrieve a list of user IDs subscribed to the topic
                String subscribedTopic=cid;
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
                            hashMap.put("message","রিপ্লাই করেছেনঃ "+comment);
                            hashMap.put("postId",postId);
                            hashMap.put("uName",name);
                            hashMap.put("uDp",image);
                            hashMap.put("userId",userId);


                            ref.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ReplyActivityForCustomerPosts.this, "Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ReplyActivityForCustomerPosts.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ReplyActivityForCustomerPosts.this, "Success", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(ReplyActivityForCustomerPosts.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(ReplyActivityForCustomerPosts.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    boolean mProcessComment=false;


    private void updateCommentCount() {
        mProcessComment=true;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments").child(cid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessComment){
                    String comments=""+snapshot.child("pReplies").getValue();
                    int newCommentVal=Integer.parseInt(comments)+1;
                    ref.child("pReplies").setValue(""+newCommentVal);
                    mProcessComment=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void loadReplies() {

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        repliesList=new ArrayList<>();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId).child("Comments")
                .child(cid).child("Replies");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                repliesList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Replies replies=ds.getValue(Replies.class);

                    repliesList.add(replies);

                    adapterRepliesForDriver=new AdapterRepliesForDriver(getApplicationContext(),repliesList);

                    recyclerView.setAdapter(adapterRepliesForDriver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}