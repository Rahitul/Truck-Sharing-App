package com.example.projectmove.Utilis.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Activity.SeeHistoryRunningDetailsActivity;
import com.example.projectmove.Map.MapsTestActivity;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.RunningHistory;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AdapterRunningHistory extends RecyclerView.Adapter<AdapterRunningHistory.MyHolder>{

    Context context;
    List<RunningHistory> runningHistoryList;

    public AdapterRunningHistory(Context context,List<RunningHistory> runningHistoryList){
        this.context=context;
        this.runningHistoryList=runningHistoryList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_running_history_short,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String didPostName=runningHistoryList.get(position).getDidPostName();
        String pTime=runningHistoryList.get(position).getpTime();
        String didPostDp=runningHistoryList.get(position).getDidPostDp();
        String pDName=runningHistoryList.get(position).getpDName();
        String pDLocation=runningHistoryList.get(position).getpDLocation();
        String pDDestination=runningHistoryList.get(position).getpDDestination();
        String pDStartingTime=runningHistoryList.get(position).getpDStartingTime();
        String pDStartingDate=runningHistoryList.get(position).getpDStartingDate();
        String customerName=runningHistoryList.get(position).getCustomerName();
        String customerPhone=runningHistoryList.get(position).getCustomerPhone();
        String customerDp=runningHistoryList.get(position).getCustomerDp();
        String pDExpectedPrice=runningHistoryList.get(position).getpDExpectedPrice();
        String driverPostId=runningHistoryList.get(position).getDriverPostId();
        String driveruid=runningHistoryList.get(position).getDriveruid();
        String timestamp=runningHistoryList.get(position).getTimestamp();
        String rHId=timestamp;



        holder.rHDriverLocation.setText(pDLocation);
        holder.rHDriverDestination.setText(pDDestination);
        holder.rHDriverStartingTime.setText(pDStartingTime);
        holder.rHDriverStartingDate.setText(pDStartingDate);

        holder.cardViewSeeHistoryRunningDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SeeHistoryRunningDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rHId",timestamp);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return runningHistoryList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView rHDriverLocation,rHDriverDestination
                ,rHDriverStartingTime,rHDriverStartingDate;
        CardView cardViewSeeHistoryRunningDetails;

        public MyHolder(@NonNull View itemView) {
            super(itemView);


            rHDriverLocation=itemView.findViewById(R.id.rHDriverLocation);
            rHDriverDestination=itemView.findViewById(R.id.rHDriverDestination);
            rHDriverStartingTime=itemView.findViewById(R.id.rHDriverStartingTime);
            rHDriverStartingDate=itemView.findViewById(R.id.rHDriverStartingDate);

            cardViewSeeHistoryRunningDetails=itemView.findViewById(R.id.cardViewSeeHistoryRunningDetails);



        }
    }

}
