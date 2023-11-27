package com.example.projectmove.Utilis.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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


import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Activity.CustomerPostDetailsActivity;
import com.example.projectmove.Activity.DriverPostDetailsActivity;
import com.example.projectmove.Activity.MainActivity;
import com.example.projectmove.Activity.ReplyActivityForCustomerPosts;
import com.example.projectmove.Activity.SeeProfileOfDrivers;
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
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class AdapterCommentsForDriver  extends RecyclerView.Adapter<AdapterCommentsForDriver.MyHolder> {

    Context context;
    List<Comments> commentList;
    String myUid,postId,myPhone,myName,myDp;

    public AdapterCommentsForDriver(Context context, List<Comments> commentList, String myUid, String postId, String myPhone, String myName, String myDp) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
        this.myPhone = myPhone;
        this.myName = myName;
        this.myDp = myDp;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_comment_for_driver,parent,false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        checkUserStatus();
        loadUserInfo();

        String uid=commentList.get(position).getUid();
        String name=commentList.get(position).getuName();
        String uPhone=commentList.get(position).getuPhone();
        String image=commentList.get(position).getuDp();
        String cid=commentList.get(position).getcId();
        String comment=commentList.get(position).getComment();
        String timestamp=commentList.get(position).getTimestamp();
        String commentSub=commentList.get(position).getCommentSub();
        String vehicleModel=commentList.get(position).getVehicleModel();
        String postId=commentList.get(position).getPostId();
        String pReplies=commentList.get(position).getpReplies();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        holder.DnameTV.setText(name);
        holder.DcommentTV.setText(comment);
        holder.DtimeTV.setText(pTime);
        holder.DVehicleModelTV.setText(vehicleModel);
        holder.DReplyCountTV.setText(pReplies+" রিপ্লাই");

        try{
            Picasso.get().load(image).placeholder(R.drawable.avatar).into(holder.DavatarIV);
        }catch (Exception e){

        }



        holder.DReplyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ReplyActivityForCustomerPosts.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("cid",cid);
                intent.putExtra("uid",uid);
                intent.putExtra("name",name);
                intent.putExtra("uPhone",uPhone);
                intent.putExtra("image",image);
                intent.putExtra("comment",comment);
                intent.putExtra("timestamp",timestamp);
                intent.putExtra("commentSub",commentSub);
                intent.putExtra("vehicleModel",vehicleModel);
                intent.putExtra("postId",postId);
                intent.putExtra("pReplies",pReplies);
                context.startActivity(intent);
            }
        });



        String timeStamp=String.valueOf(System.currentTimeMillis());
        String hi="hi";

        final String[] driverName = new String[1];
        final String[] driverPhone = new String[1];
        final String[] CUserName = new String[1];
        final String[] pCVehicleType = new String[1];
        final String[] pCStartingDate = new String[1];
        final String[] pCStartingTime = new String[1];
        final String[] pCPrice = new String[1];
        final String[] pCLocation = new String[1];
        final String[] pCDestination = new String[1];
        final String[] CDp = new String[1];
        final String[] pCName = new String[1];
        final String[] PTime = new String[1];
        final String[] cUPhone = new String[1];
        final String[] cUId = new String[1];
        final String[] driverDp = new String[1];
        final String[] cPId = new String[1];
        final String[] driveruid = new String[1];
        final String[] timer = new String[1];
        final String[] value = new String[1];
        final String[] mName = new String[1];



        //loadDriverInfo
        DatabaseReference refForDriver= FirebaseDatabase.getInstance().getReference("Users").child(uPhone);
        refForDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("username") != null) {
                        driverName[0] = map.get("username").toString();

                    }

                    if (map.get("phone") != null) {
                        driverPhone[0] = map.get("phone").toString();

                    }

                    if (map.get("imageUrl") != null) {
                        driverDp[0] = map.get("imageUrl").toString();

                    }

                    if (map.get("uid") != null) {
                        driveruid[0] = map.get("uid").toString();

                    }

                }
            }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        //loadPost
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");
        Query query=ref.orderByChild("cPId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    pCVehicleType[0] =""+ds.child("pCVehicleType").getValue();
                    pCStartingDate[0] =""+ds.child("pCStartingDate").getValue();
                    pCStartingTime[0] =""+ds.child("pCStartingTime").getValue();
                    pCPrice[0] =""+ds.child("pCPrice").getValue();
                    pCLocation[0] =""+ds.child("pCLocation").getValue();
                    pCDestination[0] =""+ds.child("pCDestination").getValue();
                    CDp[0] =""+ds.child("CDp").getValue(String.class);
                    pCName[0] =""+ds.child("pCName").getValue();
                    CUserName[0] =""+ds.child("CUserName").getValue();
                    String CPTime=""+ds.child("CPTime").getValue();
                    cUPhone[0] =""+ds.child("cUPhone").getValue();
                    cUId[0] =""+ds.child("cUId").getValue();

                    timer[0] = "" + ds.child("timer").getValue(long.class);
                    long timeInMillis = Long.parseLong(timer[0]);

                    if(commentSub.equals("")){
                        holder.DacceptBtn.setVisibility(View.GONE);
                        holder.DVehicleModelTV.setVisibility(View.GONE);
                    }
                    else {
                        holder.DacceptBtn.setVisibility(View.VISIBLE);
                    }




                    Calendar calendar=Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(CPTime));
                    PTime[0] = DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUid.equals(uid)){
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

        holder.DacceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("গ্রহন করুন");
                builder.setMessage(commentSub+" টাকায় গ্রহন করবেন?");
                builder.setPositiveButton("গ্রহন করুন", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Notification
                        PushNotification pushNotification = new PushNotification(new NotificationData(name, "আপনার অফার টি "+commentSub+" টাকায় গ্রহন করেছেন"), "/topics/"+cid+"acceptCustomer");
                        sendNotification(pushNotification);



                        //AddAcceptance
                        DatabaseReference refForAcceptance=FirebaseDatabase.getInstance().getReference("RunningHistory");

                        HashMap<String,Object> hashMap=new HashMap<>();

                        hashMap.put("pDName",pCName[0]);
                        hashMap.put("isAId",timeStamp);
                        hashMap.put("driverName", driverName[0]);
                        hashMap.put("customerDp", CDp[0]);
                        hashMap.put("customerName",CUserName[0]);
                        hashMap.put("customerPhone",cUPhone[0]);
                        hashMap.put("customeruid",cUId[0]);
                        hashMap.put("driverDp",driverDp[0]);
                        hashMap.put("driverPhone",driverPhone[0]);
                        hashMap.put("driverPostId",cPId[0]);
                        hashMap.put("driveruid",driveruid[0]);
                        hashMap.put("isAccepted","isAccepted");
                        hashMap.put("pDDestination",pCDestination[0]);
                        hashMap.put("pDExpectedPrice",pCPrice[0]);
                        hashMap.put("pDLocation",pCLocation[0]);
                        hashMap.put("pDStartingDate",pCStartingDate[0]);
                        hashMap.put("pDStartingTime",pCStartingTime[0]);
                        hashMap.put("pTime",PTime[0]);
                        hashMap.put("timestamp",timeStamp);



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
                                            value[0] =snapshot.getValue(String.class);
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












                                    checkUserStatus();
                                    loadUserInfo();
                                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notifications").child(userId).child(timestamp);


                                    HashMap<String,Object> hashMap=new HashMap<>();


                                    hashMap.put("timeStamp",timestamp);
                                    hashMap.put("nId",timestamp);
                                    hashMap.put("message","আপনার অফার টি "+commentSub+" টাকায় গ্রহন করেছেন");
                                    hashMap.put("postId",postId);
                                    hashMap.put("uName",myName);
                                    hashMap.put("uDp", myDp);
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

                        DatabaseReference refaddAcceptanceId=FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId);

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

        }
    }


    private void deleteComment(String cid) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts").child(postId);
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

        CircleImageView DavatarIV;
        TextView DnameTV,DcommentTV,DtimeTV;
        TextView DVehicleModelTV,DReplyTV,DReplyCountTV;
        ImageView DacceptBtn;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            DavatarIV=itemView.findViewById(R.id.DavatarIV);
            DnameTV=itemView.findViewById(R.id.DnameTV);
            DcommentTV=itemView.findViewById(R.id.DcommentTV);
            DtimeTV=itemView.findViewById(R.id.DtimeTV);
            DacceptBtn=itemView.findViewById(R.id.DacceptBtn);
            DVehicleModelTV=itemView.findViewById(R.id.DVehicleModelTV);
            DReplyTV=itemView.findViewById(R.id.DReplyTV);
            DReplyCountTV=itemView.findViewById(R.id.DReplyCountTV);
        }
    }

}


