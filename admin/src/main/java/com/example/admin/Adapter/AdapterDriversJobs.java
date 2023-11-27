package com.example.admin.Adapter;

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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Activity.DriverPostsDetailsActivity;
import com.example.admin.Class.DriversJobs;
import com.example.admin.R;
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
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDriversJobs extends RecyclerView.Adapter<AdapterDriversJobs.MyHolder> {

    Context context;
    List<DriversJobs> driversJobLists;
    FirebaseAuth auth;
    FirebaseUser user;
    String myUid;

    public AdapterDriversJobs(Context context, List<DriversJobs> driversJobLists){
        this.context=context;
        this.driversJobLists=driversJobLists;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_posts_of_driver,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        myUid=user.getUid();


        String uid= driversJobLists.get(position).getUid();
        String uPhone= driversJobLists.get(position).getuPhone();
        String uDp= driversJobLists.get(position).getuDp();
        String uName= driversJobLists.get(position).getuName();
        String pTimeStamp= driversJobLists.get(position).getpTime();
        String pDStartingDate= driversJobLists.get(position).getpDStartingDate();
        String pDStartingTime= driversJobLists.get(position).getpDStartingTime();
        String pDExpectedPrice= driversJobLists.get(position).getpDExpectedPrice();
        String pDDestination= driversJobLists.get(position).getpDDestination();
        String pDLocation= driversJobLists.get(position).getpDLocation();
        String pDName= driversJobLists.get(position).getpDName();
        String pId= driversJobLists.get(position).getpId();
        String pComments=driversJobLists.get(position).getpComments();
        String pDNumberPlate=driversJobLists.get(position).getpDNumberPlate();
        String pDVehicleType=driversJobLists.get(position).getpDVehicleType();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        holder.uNameTV.setText(uName);
        holder.pTimeTV.setText(pTime);
        holder.pDriverName.setText(pDName);
        holder.pDriverLocation.setText(pDLocation);
        holder.pDriverDestination.setText(pDDestination);
        holder.pDriverExpectedCost.setText(pDExpectedPrice);
        holder.pDriverStartingTime.setText(pDStartingTime);
        holder.pDriverStartingDate.setText(pDStartingDate);
        holder.pCommentsTV.setText(pComments+"কমেন্ট");
        holder.pDriverNumberPlate.setText(pDNumberPlate);
        holder.pDriverVehicleType.setText(pDVehicleType);

        try{
            Picasso.get().load(uDp).placeholder(R.drawable.avatar).into(holder.uPictureIV);
        }
        catch (Exception e){

        }



        



        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUid.equals(uid)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("পোস্ট ডিলিট");
                    builder.setMessage("পোস্টটি কি ডিলিট করতে চান?");
                    builder.setPositiveButton("ডিলিট", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deletePost(pId);
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

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, DriverPostsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("postId",pId);
                intent.putExtra("driverPhone",uPhone);
                context.startActivity(intent);

            }
        });

        /*holder.seeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, SeeProfileOfDrivers.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("driveruid",uid);
                intent.putExtra("driverPhone",uPhone);
                context.startActivity(intent);
            }
        });*/



    }

    private void deletePost(String pId) {

        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("DriversPosts");
        ref.child(pId).removeValue();

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
        return driversJobLists.size();
    }

    public void filterList(ArrayList<DriversJobs> filteredList) {
        driversJobLists = filteredList;
        notifyDataSetChanged();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView uPictureIV;
        TextView uNameTV,pTimeTV,pDriverName,pDriverLocation,pDriverDestination,pDriverExpectedCost,pDriverStartingTime,pDriverStartingDate,pCommentsTV,pDriverNumberPlate,pDriverVehicleType;
        ImageView moreButton;

        CardView commentButton;
        LinearLayout seeProfile;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            uPictureIV=itemView.findViewById(R.id.uPictureIV);
            uNameTV=itemView.findViewById(R.id.uNameTV);
            pTimeTV=itemView.findViewById(R.id.pTimeTV);
            pDriverName=itemView.findViewById(R.id.pDriverName);
            pDriverLocation=itemView.findViewById(R.id.pDriverLocation);
            pDriverDestination=itemView.findViewById(R.id.pDriverDestination);
            pDriverExpectedCost=itemView.findViewById(R.id.pDriverExpectedCost);
            pDriverStartingTime=itemView.findViewById(R.id.pDriverStartingTime);
            pDriverStartingDate=itemView.findViewById(R.id.pDriverStartingDate);
            pCommentsTV=itemView.findViewById(R.id.pCommentsTV);

            moreButton=itemView.findViewById(R.id.moreButton);
            commentButton=itemView.findViewById(R.id.cardView);
            seeProfile=itemView.findViewById(R.id.seeProfile);


            pDriverNumberPlate=itemView.findViewById(R.id.pDriverNumberPlate);
            pDriverVehicleType=itemView.findViewById(R.id.pDriverVehicleType);
        }
    }
}
