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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Activity.SeeProfileOfDrivers;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.AmbulanceJobs;
import com.example.projectmove.Utilis.Class.DriversJobs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class AdapterAmbulanceJobs extends RecyclerView.Adapter<AdapterAmbulanceJobs.MyHolder> {

    Context context;
    List<AmbulanceJobs> ambulanceJobsList;
    FirebaseAuth auth;
    FirebaseUser user;
    String myUid;

    public AdapterAmbulanceJobs(Context context,List<AmbulanceJobs> ambulanceJobsList){
        this.context=context;
        this.ambulanceJobsList=ambulanceJobsList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_ambulance_posts,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        String pid,uid,isAccepted,uPhone,pVehicleModel,pPrice,pLocation,pDestination,pName,userName,pTime,pDp,pComments,vehicleNumber;

        pid=ambulanceJobsList.get(position).getPid();
        uid=ambulanceJobsList.get(position).getUid();
        isAccepted=ambulanceJobsList.get(position).getIsAccepted();
        uPhone=ambulanceJobsList.get(position).getuPhone();
        pVehicleModel=ambulanceJobsList.get(position).getpVehicleModel();
        pPrice=ambulanceJobsList.get(position).getpPrice();
        pLocation=ambulanceJobsList.get(position).getpLocation();
        pDestination=ambulanceJobsList.get(position).getpDestination();
        pName=ambulanceJobsList.get(position).getpName();
        userName=ambulanceJobsList.get(position).getUserName();
        pTime=ambulanceJobsList.get(position).getpTime();
        pDp=ambulanceJobsList.get(position).getpDp();
        pComments=ambulanceJobsList.get(position).getpComments();
        vehicleNumber=ambulanceJobsList.get(position).getVehicleNumber();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTime));
        String PTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();


        holder.pVehicleModel.setText(pVehicleModel);
        holder.expectedPrice.setText(pPrice);
        holder.pLocation.setText(pLocation);
        holder.pHospitalName.setText(pDestination);
        holder.pName.setText(pName);
        holder.myName.setText(userName);
        holder.pTime.setText(PTime);
        holder.pVehicleNumber.setText(vehicleNumber);

        try{
            Picasso.get().load(pDp).placeholder(R.drawable.avatar).into(holder.myDp);
        }
        catch (Exception e){

        }

        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        String myPhone=user.getPhoneNumber();
        myUid=user.getUid();


        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUid.equals(uid)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("পোস্ট ডিলিট");
                    builder.setMessage("পোস্টটি কি ডিলিট করতে চান?");
                    builder.setPositiveButton("ডিলিট", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deletePost(pid);
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


                else{
                    Toast.makeText(context, "দুঃখিত! অন্যের পোস্ট ডিলিট করা যাবে না", Toast.LENGTH_SHORT).show();
                }

            }
            });


        holder.seeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SeeProfileOfDrivers.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("driveruid",uid);
                intent.putExtra("driverPhone",uPhone);
                context.startActivity(intent);
            }
        });



        final String[] customerName = new String[1];
        final String[] customerPhone = new String[1];
        final String[] customerDp = new String[1];
        final String[] customeruid = new String[1];



        //loadCustomerInfo
        DatabaseReference refForDriver= FirebaseDatabase.getInstance().getReference("Users").child(myPhone);
        refForDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("username") != null) {
                        customerName[0] = map.get("username").toString();

                    }

                    if (map.get("phone") != null) {
                        customerPhone[0] = map.get("phone").toString();

                    }

                    if (map.get("imageUrl") != null) {
                        customerDp[0] = map.get("imageUrl").toString();

                    }

                    if (map.get("uid") != null) {
                        customeruid[0] = map.get("uid").toString();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        String timeStamp=String.valueOf(System.currentTimeMillis());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("গ্রহন করুন");
                builder.setMessage(pPrice+" টাকায় গ্রহন করবেন?");
                builder.setPositiveButton("গ্রহন করুন", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        //AddAcceptance
                        DatabaseReference refForAcceptance= FirebaseDatabase.getInstance().getReference("RunningHistory");

                        HashMap<String,Object> hashMap=new HashMap<>();

                        hashMap.put("isAId",timeStamp);
                        hashMap.put("driverPostId",pid);
                        hashMap.put("isAccepted","Accepted");
                        hashMap.put("timestamp",timeStamp);
                        hashMap.put("customeruid",customeruid[0]);
                        hashMap.put("customerPhone",customerPhone[0]);
                        hashMap.put("customerDp",customerDp[0]);
                        hashMap.put("customerName",customerName[0]);


                        hashMap.put("pDName",pName);
                        hashMap.put("pDLocation",pLocation);
                        hashMap.put("pDDestination",pDestination);
                        hashMap.put("pDExpectedPrice",pPrice);
                        hashMap.put("pDStartingTime","");
                        hashMap.put("pDStartingDate","");

                        hashMap.put("driverDp",pDp);
                        hashMap.put("driveruid",uid);
                        hashMap.put("driverName",userName);
                        hashMap.put("driverPhone",uPhone);

                        hashMap.put("pTime",PTime);



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


                        //AddAcceptanceId

                        DatabaseReference refaddAcceptanceId=FirebaseDatabase.getInstance().getReference("AmbulancePosts").child(pid);

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

    private void deletePost(String pid) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("AmbulancePosts");
        ref.child(pid).removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(context, "সফল ভাবে ডিলিট হয়েছে", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ambulanceJobsList.size();
    }

    public void filterList(ArrayList<AmbulanceJobs> filteredList) {
        ambulanceJobsList = filteredList;
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView myDp;
        TextView myName,pTime,expectedPrice,pName,pLocation,pHospitalName,pVehicleModel,pVehicleNumber;
        ImageView moreBtn;
        TextView accept;
        LinearLayout seeProfile;


        public MyHolder(View itemView) {
            super(itemView);

            myDp=itemView.findViewById(R.id.uPictureIVAmbulancePosts);
            myName=itemView.findViewById(R.id.uNameTVAmbulancePosts);
            pTime=itemView.findViewById(R.id.pTimeTVAmbulancePosts);
            expectedPrice=itemView.findViewById(R.id.pDriverExpectedCostAmbulancePosts);
            pName=itemView.findViewById(R.id.pDriverNameAmbulancePosts);
            pLocation=itemView.findViewById(R.id.pDriverLocationAmbulancePosts);
            pHospitalName=itemView.findViewById(R.id.pDriverDestinationAmbulancePosts);
            pVehicleModel=itemView.findViewById(R.id.pDriverVehicleModelAmbulancePosts);
            pVehicleNumber=itemView.findViewById(R.id.pDriverNumberPlateAmbulancePosts);
            moreBtn=itemView.findViewById(R.id.moreButtonAmbulancePosts);
            seeProfile=itemView.findViewById(R.id.seeProfileAmbulancePosts);
            accept=itemView.findViewById(R.id.acceptBtnAmbulancePosts);
        }
    }


}
