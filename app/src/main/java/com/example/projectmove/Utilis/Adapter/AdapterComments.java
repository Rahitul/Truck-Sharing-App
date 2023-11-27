package com.example.projectmove.Utilis.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Activity.CustomerPostDetailsActivity;
import com.example.projectmove.Activity.ReplyActivityForCustomerPosts;
import com.example.projectmove.Activity.ReplyActivityForDriverPosts;
import com.example.projectmove.Model.NotificationData;
import com.example.projectmove.Model.PushNotification;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.Comments;
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

public class AdapterComments  extends RecyclerView.Adapter<AdapterComments.MyHolder> {

    Context context;
    List<Comments> commentList;
    String myUid,postId;
    String myDp,myName,myPhone,myuid;


    public AdapterComments(Context context, List<Comments> commentList, String myUid, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        checkUserStatus();
        loadUserInfo();

        final String[] uid = {commentList.get(position).getUid()};
        String name=commentList.get(position).getuName();
        String phone=commentList.get(position).getuPhone();
        String image=commentList.get(position).getuDp();
        String cid=commentList.get(position).getcId();
        String comment=commentList.get(position).getComment();
        String timestamp=commentList.get(position).getTimestamp();
        String commentSub=commentList.get(position).getCommentSub();
        String pReplies=commentList.get(position).getpReplies();
        String postId=commentList.get(position).getPostId();


        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        holder.nameTV.setText(name);
        holder.commentTV.setText(comment);
        holder.timeTV.setText(pTime);
        holder.replyCountTV.setText(pReplies+" রিপ্লাই");


        final String[] pDVehicleType = new String[1];
        final String[] pDStartingDate = new String[1];
        final String[] pDStartingTime = new String[1];
        final String[] pDExpectedPrice = new String[1];
        final String[] pDLocation = new String[1];
        final String[] pDDestination = new String[1];
        final String[] uDp = new String[1];
        final String[] pDName = new String[1];
        final String[] uName = new String[1];
        final String[] uPhone = new String[1];
        final String[] dUid = new String[1];
        final String[] PTime = new String[1];
        final String[] PostId = new String[1];
        final String[] value = new String[1];
        final String[] mName = new String[1];


        String timeStamp=String.valueOf(System.currentTimeMillis());




        //loadPost
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");
        Query query=ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    pDVehicleType[0] =""+ds.child("pDVehicleType").getValue();
                    pDStartingDate[0] =""+ds.child("pDStartingDate").getValue();
                    pDStartingTime[0] =""+ds.child("pDStartingTime").getValue();
                    pDExpectedPrice[0] =""+ds.child("pDExpectedPrice").getValue();
                    pDLocation[0] =""+ds.child("pDLocation").getValue();
                    pDDestination[0] =""+ds.child("pDDestination").getValue();
                    uDp[0] =""+ds.child("uDp").getValue(String.class);
                    pDName[0] =""+ds.child("pDName").getValue();
                    uName[0] =""+ds.child("uName").getValue();
                    String pTime=""+ds.child("pTime").getValue();
                    uPhone[0] =""+ds.child("uPhone").getValue();
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        holder.replyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ReplyActivityForDriverPosts.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("cid",cid);
                intent.putExtra("uid",uid);
                intent.putExtra("name",name);
                intent.putExtra("uPhone",uPhone);
                intent.putExtra("image",image);
                intent.putExtra("comment",comment);
                intent.putExtra("timestamp",timestamp);
                intent.putExtra("commentSub",commentSub);
                intent.putExtra("postId",postId);
                intent.putExtra("pReplies",pReplies);
                context.startActivity(intent);
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
                        PushNotification pushNotification = new PushNotification(new NotificationData(name, "আপনার অফার টি "+commentSub+" টাকায় গ্রহন করেছেন"), "/topics/"+cid+"acceptDriver");
                        sendNotification(pushNotification);



                        //AddAcceptance
                        DatabaseReference refForAcceptance=FirebaseDatabase.getInstance().getReference("RunningHistory");

                        HashMap<String,Object> hashMap=new HashMap<>();

                        hashMap.put("isAId",timeStamp);
                        hashMap.put("driverPostId",postId);
                        hashMap.put("isAccepted","Accepted");
                        hashMap.put("timestamp",timeStamp);
                        hashMap.put("customeruid", uid[0]);
                        hashMap.put("customerPhone",phone);
                        hashMap.put("customerDp",image);
                        hashMap.put("customerName",name);


                        hashMap.put("pDName",pDName[0]);
                        hashMap.put("pDLocation",pDLocation[0]);
                        hashMap.put("pDDestination",pDDestination[0]);
                        hashMap.put("pDExpectedPrice",pDExpectedPrice[0]);
                        hashMap.put("pDStartingTime",pDStartingTime[0]);
                        hashMap.put("pDStartingDate",pDStartingDate[0]);

                        hashMap.put("driverDp",uDp[0]);
                        hashMap.put("driveruid",dUid[0]);
                        hashMap.put("driverName",uName[0]);
                        hashMap.put("driverPhone",uPhone[0]);

                        hashMap.put("pTime",PTime[0]);
                        hashMap.put("cid",cid);



                        refForAcceptance.child(timeStamp).setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });





                        // Retrieve a list of user IDs subscribed to the topic
                        String subscribedTopic=cid+"Accept";
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

                                    String phone=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();




                                    FirebaseDatabase database;
                                    DatabaseReference myRef;
                                    database= FirebaseDatabase.getInstance();
                                    myRef=database.getReference("Users");
                                    myRef.child(phone).child("imageUrl").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            value[0]=snapshot.getValue(String.class);
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
                                                    mName[0]=map.get("username").toString();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });













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
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        try{
            Picasso.get().load(image).placeholder(R.drawable.avatar).into(holder.avatarIV);
        }catch (Exception e){

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUid.equals(uid[0])){
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure to delete");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteComment(cid);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.create().show();

                }
                else{
                    Toast.makeText(context, "Can't delete other comment", Toast.LENGTH_SHORT).show();
                }
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
            myuid=user.getUid();

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

    private void deleteComment(String cid) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts").child(postId);
        ref.child("Comments").child(cid).removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String comments=""+snapshot.child("pComments").getValue();
                int newCommentVal=Integer.parseInt(comments)-1;
                ref.child("pComments").setValue(""+newCommentVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView avatarIV;
        TextView nameTV,commentTV,timeTV,replyTV,replyCountTV;
        CardView cardViewComments;
        ImageView acceptBtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            avatarIV=itemView.findViewById(R.id.avatarIV);
            nameTV=itemView.findViewById(R.id.nameTV);
            commentTV=itemView.findViewById(R.id.commentTV);
            timeTV=itemView.findViewById(R.id.timeTV);
            replyTV=itemView.findViewById(R.id.replyTV);
            acceptBtn=itemView.findViewById(R.id.acceptBtn);
            cardViewComments=itemView.findViewById(R.id.cardViewComments);
            replyCountTV=itemView.findViewById(R.id.replyCountTV);
        }
    }

}

