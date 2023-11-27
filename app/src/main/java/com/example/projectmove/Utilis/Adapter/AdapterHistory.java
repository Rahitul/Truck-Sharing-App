package com.example.projectmove.Utilis.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.Activity.CustomerPostDetailsActivity;
import com.example.projectmove.Activity.SeeHistoryDetailsActivity;
import com.example.projectmove.Activity.SeeHistoryRunningDetailsActivity;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.History;
import com.example.projectmove.Utilis.Class.RunningHistory;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyHolder>{

    Context context;
    List<History> HistoryList;

    public AdapterHistory(Context context,List<History> HistoryList){
        this.context=context;
        this.HistoryList=HistoryList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_history_short_details,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        String pDLocation=HistoryList.get(position).getpDLocation();
        String pDDestination=HistoryList.get(position).getpDDestination();
        String pDStartingTime=HistoryList.get(position).getpDStartingTime();
        String pDStartingDate=HistoryList.get(position).getpDStartingDate();
        String timestamp=HistoryList.get(position).getTimestamp();
        String rHId=timestamp;



        holder.HDriverLocation.setText(pDLocation);
        holder.HDriverDestination.setText(pDDestination);


        holder.cardViewSeeHistoryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SeeHistoryDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rHId",timestamp);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView HDriverLocation,HDriverDestination;
        CardView cardViewSeeHistoryDetails;

        public MyHolder(@NonNull View itemView) {
            super(itemView);


            HDriverLocation=itemView.findViewById(R.id.HDriverLocation);
            HDriverDestination=itemView.findViewById(R.id.HDriverDestination);


            cardViewSeeHistoryDetails=itemView.findViewById(R.id.cardViewSeeHistoryDetails);



        }
    }

}
