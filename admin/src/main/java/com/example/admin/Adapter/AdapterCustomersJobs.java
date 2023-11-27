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
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Activity.CustomerPostsDetailsActivity;
import com.example.admin.Class.CustomersJobs;
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

public class AdapterCustomersJobs extends RecyclerView.Adapter<AdapterCustomersJobs.MyHolder> {

    Context context;
    List<CustomersJobs> customersJobsLists;
    FirebaseAuth auth;
    FirebaseUser user;

    public AdapterCustomersJobs(Context context, List<CustomersJobs> customersJobsLists){
        this.context=context;
        this.customersJobsLists=customersJobsLists;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_posts_of_customer,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String cPId=customersJobsLists.get(position).getcPId();
        String cUId=customersJobsLists.get(position).getcUId();
        String cUPhone=customersJobsLists.get(position).getcUPhone();
        String pCVehicleType=customersJobsLists.get(position).getpCVehicleType();
        String pCCalculatedDistance=customersJobsLists.get(position).getpCCalculatedDistance();
        String pCStartingDate=customersJobsLists.get(position).getpCStartingDate();
        String pCStartingTime=customersJobsLists.get(position).getpCStartingTime();
        String pCPrice=customersJobsLists.get(position).getpCPrice();
        String pCLocation=customersJobsLists.get(position).getpCLocation();
        String pCDestination=customersJobsLists.get(position).getpCDestination();
        String pCName=customersJobsLists.get(position).getpCName();
        String CUserName=customersJobsLists.get(position).getCUserName();
        String CPTime=customersJobsLists.get(position).getCPTime();
        String CDp=customersJobsLists.get(position).getCDp();
        String pComments=customersJobsLists.get(position).getpComments();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(CPTime));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        try{
            Picasso.get().load(CDp).placeholder(R.drawable.avatar).into(holder.uCPictureIV);
        }
        catch (Exception e){

        }

        holder.uCNameTV.setText(CUserName);
        holder.pCTimeTV.setText(pTime);
        holder.pCustomerName.setText(pCName);
        holder.pCustomerDestination.setText(pCDestination);
        holder.pCustomerLocation.setText(pCLocation);
        holder.pCustomerCost.setText(pCPrice);
        holder.pCustomerStartingTime.setText(pCStartingTime);
        holder.pCustomerStartingDate.setText(pCStartingDate);
        holder.pCustomerVehicleType.setText(pCVehicleType);
        holder.pCCommentsTV.setText(pComments+" কমেন্ট");

        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        assert user != null;
        String myUid=user.getUid();



        holder.customerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CustomerPostsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("postId",cPId);
                intent.putExtra("customerPhone",cUPhone);
                context.startActivity(intent);
            }
        });


        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUid.equals(cUId)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("পোস্ট ডিলিট");
                    builder.setMessage("পোস্টটি কি ডিলিট করতে চান?");
                    builder.setPositiveButton("ডিলিট", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deletePost(cPId);
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


    }

    private void deletePost(String cPId) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("CustomerPosts");
        ref.child(cPId).removeValue();

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
        return customersJobsLists.size();
    }

    public void filterList(ArrayList<CustomersJobs> filteredList) {
        customersJobsLists = filteredList;
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView uCPictureIV;
        TextView uCNameTV,pCTimeTV,pCustomerCost,pCustomerName,pCustomerLocation,pCustomerDestination,pCustomerStartingTime,
                pCustomerStartingDate,pCustomerVehicleType,pCCommentsTV;
        CardView customerCardView;
        ImageView moreButton;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uCPictureIV=itemView.findViewById(R.id.uCPictureIV);
            uCNameTV=itemView.findViewById(R.id.uCNameTV);
            pCTimeTV=itemView.findViewById(R.id.pCTimeTV);
            pCustomerCost=itemView.findViewById(R.id.pCustomerCost);
            pCustomerName=itemView.findViewById(R.id.pCustomerName);
            pCustomerLocation=itemView.findViewById(R.id.pCustomerLocation);
            pCustomerDestination=itemView.findViewById(R.id.pCustomerDestination);
            pCustomerStartingTime=itemView.findViewById(R.id.pCustomerStartingTime);
            pCustomerStartingDate=itemView.findViewById(R.id.pCustomerStartingDate);
            pCustomerVehicleType=itemView.findViewById(R.id.pCustomerVehicleType);
            customerCardView=itemView.findViewById(R.id.customerCardView);
            pCCommentsTV=itemView.findViewById(R.id.pCCommentsTV);
            moreButton=itemView.findViewById(R.id.moreCustomerButton);
        }
    }
}
