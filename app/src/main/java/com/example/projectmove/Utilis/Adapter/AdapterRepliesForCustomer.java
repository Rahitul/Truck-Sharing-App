package com.example.projectmove.Utilis.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Model.NotificationData;
import com.example.projectmove.Model.PushNotification;
import com.example.projectmove.R;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;

public class AdapterRepliesForCustomer extends RecyclerView.Adapter<AdapterRepliesForCustomer.MyHolder> {

    Context context;
    List<Replies> repliesList;
    String myDp,myName,myPhone,myUid;

    public AdapterRepliesForCustomer(Context context,List<Replies> repliesList){
        this.context=context;
        this.repliesList=repliesList;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_reply_for_driver_posts,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        checkUserStatus();
        loadUserInfo();

        String comment=repliesList.get(position).getComment();
        String rId=repliesList.get(position).getrId();
        String timestamp=repliesList.get(position).getTimestamp();
        String uDp=repliesList.get(position).getuDp();
        String uName=repliesList.get(position).getuName();
        String uPhone=repliesList.get(position).getuPhone();
        String uid=repliesList.get(position).getUid();
        String postId=repliesList.get(position).getPostId();
        String commentSub=repliesList.get(position).getCommentSub();
        String cid=repliesList.get(position).getCid();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        try{
            Picasso.get().load(uDp).placeholder(R.drawable.avatar).into(holder.image);
        }
        catch (Exception e){

        }

        holder.userName.setText(uName);
        holder.comment.setText(comment);
        holder.cTime.setText(pTime);

        String timeStamp=String.valueOf(System.currentTimeMillis());

        final String[] pDVehicleType = new String[1];
        final String[] pDStartingDate = new String[1];
        final String[] pDStartingTime = new String[1];
        final String[] pDExpectedPrice = new String[1];
        final String[] pDLocation = new String[1];
        final String[] pDDestination = new String[1];
        final String[] uDpA = new String[1];
        final String[] pDName = new String[1];
        final String[] uNameA = new String[1];
        final String[] uPhoneA = new String[1];
        final String[] dUid = new String[1];
        final String[] PTime = new String[1];
        final String[] PostId = new String[1];
        final String[] value = new String[1];
        final String[] mName = new String[1];




        String phone=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        FirebaseDatabase database;
        DatabaseReference myRef;
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");
        myRef.child(phone).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@org.checkerframework.checker.nullness.qual.NonNull DataSnapshot snapshot) {
                value[0]=snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@org.checkerframework.checker.nullness.qual.NonNull DatabaseError error) {

            }
        });








        myRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@org.checkerframework.checker.nullness.qual.NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    Map<String, Object> map=(Map<String, Object>) snapshot.getValue();
                    if(map.get("username")!=null){
                        mName[0]=map.get("username").toString();
                    }
                }

            }

            @Override
            public void onCancelled(@org.checkerframework.checker.nullness.qual.NonNull DatabaseError error) {

            }
        });















        //loadPost
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");
        Query query=ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@org.checkerframework.checker.nullness.qual.NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    pDVehicleType[0] =""+ds.child("pDVehicleType").getValue();
                    pDStartingDate[0] =""+ds.child("pDStartingDate").getValue();
                    pDStartingTime[0] =""+ds.child("pDStartingTime").getValue();
                    pDExpectedPrice[0] =""+ds.child("pDExpectedPrice").getValue();
                    pDLocation[0] =""+ds.child("pDLocation").getValue();
                    pDDestination[0] =""+ds.child("pDDestination").getValue();
                    uDpA[0] =""+ds.child("uDp").getValue(String.class);
                    pDName[0] =""+ds.child("pDName").getValue();
                    uNameA[0] =""+ds.child("uName").getValue();
                    String pTime=""+ds.child("pTime").getValue();
                    uPhoneA[0] =""+ds.child("uPhone").getValue();
                    dUid[0] =""+ds.child("uid").getValue();
                    PostId[0] =""+ds.child("pId").getValue();


                    if(commentSub.equals("")){
                        holder.acceptBtn.setVisibility(View.GONE);
                    }
                    else {
                        holder.acceptBtn.setVisibility(View.VISIBLE);
                    }




                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTime));
                    PTime[0] = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();



                }
            }

            @Override
            public void onCancelled(@org.checkerframework.checker.nullness.qual.NonNull DatabaseError error) {

            }
        });




        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("গ্রহন করুন");
                builder.setMessage(commentSub+" টাকায় গ্রহন করবেন?");
                builder.setPositiveButton("গ্রহন করুন", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Notification
                        PushNotification pushNotification = new PushNotification(new NotificationData(uName, "আপনার অফার টি "+commentSub+" টাকায় গ্রহন করেছেন"), "/topics/"+cid+"acceptDriver");
                        sendNotification(pushNotification);



                        //AddAcceptance
                        DatabaseReference refForAcceptance=FirebaseDatabase.getInstance().getReference("RunningHistory");

                        HashMap<String,Object> hashMap=new HashMap<>();

                        hashMap.put("isAId",timestamp);
                        hashMap.put("driverPostId",postId);
                        hashMap.put("isAccepted","Accepted");
                        hashMap.put("timestamp",timestamp);
                        hashMap.put("customeruid", uid);
                        hashMap.put("customerPhone",uPhone);
                        hashMap.put("customerDp",uDp);
                        hashMap.put("customerName",uName);


                        hashMap.put("pDName",pDName[0]);
                        hashMap.put("pDLocation",pDLocation[0]);
                        hashMap.put("pDDestination",pDDestination[0]);
                        hashMap.put("pDExpectedPrice",pDExpectedPrice[0]);
                        hashMap.put("pDStartingTime",pDStartingTime[0]);
                        hashMap.put("pDStartingDate",pDStartingDate[0]);

                        hashMap.put("driverDp",uDpA[0]);
                        hashMap.put("driveruid",dUid[0]);
                        hashMap.put("driverName",uNameA[0]);
                        hashMap.put("driverPhone",uPhoneA[0]);

                        hashMap.put("pTime",PTime[0]);



                        refForAcceptance.child(timeStamp).setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                        // Retrieve a list of user IDs subscribed to the topic
                        String subscribedTopic=cid+"Accept";
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String sUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference.child("Topics").child(subscribedTopic).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@org.checkerframework.checker.nullness.qual.NonNull DataSnapshot dataSnapshot) {
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
                                    hashMap.put("message","আপনার অফার টি "+commentSub+" টাকায় গ্রহন করেছেন");
                                    hashMap.put("postId",postId);
                                    hashMap.put("uName",myName);
                                    hashMap.put("uDp",myDp);
                                    hashMap.put("userId",userId);
                                    hashMap.put("uPhone",myPhone);


                                    ref.setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }

                            }

                            @Override
                            public void onCancelled(@org.checkerframework.checker.nullness.qual.NonNull DatabaseError databaseError) {

                            }
                        });





















                        //AddAcceptanceId

                        DatabaseReference refaddAcceptanceId=FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId);

                        String isAccepted="Accepted";

                        HashMap<String,Object> hashMapAddAcceptanceID=new HashMap<>();
                        hashMapAddAcceptanceID.put("isAId",timeStamp);
                        hashMapAddAcceptanceID.put("isAccepted",isAccepted);

                        refaddAcceptanceId.updateChildren(hashMapAddAcceptanceID);






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
    }

    private void loadUserInfo() {
        Query myRef=FirebaseDatabase.getInstance().getReference("Users");
        myRef.orderByChild("uid").equalTo(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@org.checkerframework.checker.nullness.qual.NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    myName=""+ds.child("username").getValue();
                    myDp=""+ds.child("imageUrl").getValue();
                }

            }

            @Override
            public void onCancelled(@org.checkerframework.checker.nullness.qual.NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            myPhone=user.getPhoneNumber();
            myUid=user.getUid();

        }
        else {

        }
    }

    private void sendNotification(PushNotification pushNotification) {
        ApiUtilities.getClient().sendNotification(pushNotification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, retrofit2.Response<PushNotification> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return repliesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

            CircleImageView image;
            TextView userName,comment,cTime,acceptBtn;

        public MyHolder(@NonNull View itemView) {
                super(itemView);

                image=itemView.findViewById(R.id.avatarReply_for_driver_postsIV);
                userName=itemView.findViewById(R.id.nameReply_for_driver_postsTV);
                comment=itemView.findViewById(R.id.commentReply_for_driver_postsTV);
                cTime=itemView.findViewById(R.id.timeReply_for_driver_postsTV);
                acceptBtn=itemView.findViewById(R.id.acceptBtn_driver_posts);

        }
    }

}
