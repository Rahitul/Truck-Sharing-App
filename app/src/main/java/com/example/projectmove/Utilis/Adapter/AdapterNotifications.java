package com.example.projectmove.Utilis.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Activity.CustomerPostDetailsActivity;
import com.example.projectmove.Activity.DriverPostDetailsActivity;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.Notifications;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.MyHolder> {

    Context context;
    List<Notifications> notificationsList;

    public AdapterNotifications(Context context, List<Notifications> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_notifications,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        String timeStamp,nId,message,postId,uName,uDp,userId,uPhone;

        timeStamp=notificationsList.get(position).getTimeStamp();
        nId=notificationsList.get(position).getnId();
        message=notificationsList.get(position).getMessage();
        postId=notificationsList.get(position).getPostId();
        uName=notificationsList.get(position).getuName();
        uDp=notificationsList.get(position).getuDp();
        userId=notificationsList.get(position).getUserId();
        uPhone=notificationsList.get(position).getuPhone();



        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String nTime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        try{
            Picasso.get().load(uDp).placeholder(R.drawable.avatar).into(holder.dp);
        }
        catch (Exception e){

        }

        holder.message.setText(message);
        holder.name.setText(uName);
        holder.time.setText(nTime);





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReferenceForCustomer= FirebaseDatabase.getInstance().getReference("CustomerPosts");
                DatabaseReference databaseReferenceForDriver= FirebaseDatabase.getInstance().getReference("DriversPosts");



                databaseReferenceForCustomer.orderByChild("cPId").equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent=new Intent(context, DriverPostDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("postId",postId);
                            intent.putExtra("customerPhone",uPhone);
                            context.startActivity(intent);
                        } else {
                            // postId does not exist in the "Customer" node
                            // Check in the "driver" node
                            databaseReferenceForDriver.orderByChild("pId").equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent intent=new Intent(context, CustomerPostDetailsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("postId",postId);
                                        intent.putExtra("driverPhone",uPhone);
                                        context.startActivity(intent);
                                    } else {
                                        // postId does not exist in both "Customer" and "driver" nodes
                                        Toast.makeText(context, "postId does not exist in both Customer and driver nodes", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle error
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView dp;
        TextView name,message,time;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            dp=itemView.findViewById(R.id.avatarIVNotifications);
            name=itemView.findViewById(R.id.nameTVNotifications);
            message=itemView.findViewById(R.id.commentTVNotifications);
            time=itemView.findViewById(R.id.timeTVNotifications);
            cardView=itemView.findViewById(R.id.cardViewNotifications);


        }
    }

}
