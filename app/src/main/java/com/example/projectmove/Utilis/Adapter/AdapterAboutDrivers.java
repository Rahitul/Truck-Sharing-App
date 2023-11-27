package com.example.projectmove.Utilis.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.AboutDrivers;
import com.example.projectmove.Utilis.Class.AmbulanceJobs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAboutDrivers extends RecyclerView.Adapter<AdapterAboutDrivers.MyHolder> {

    Context context;
    List<AboutDrivers> aboutDriversList;
    FirebaseAuth auth;
    FirebaseUser user;
    String myUid;

    public AdapterAboutDrivers(Context context,List<AboutDrivers> aboutDriversList){
        this.context=context;
        this.aboutDriversList=aboutDriversList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_about_driver_comments,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        String cTime,comment,uid,dp,name;

        cTime=aboutDriversList.get(position).getcTime();
        comment=aboutDriversList.get(position).getComment();
        uid=aboutDriversList.get(position).getUid();
        dp=aboutDriversList.get(position).getDp();
        name=aboutDriversList.get(position).getName();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(cTime));
        String ctime= DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar).toString();

        holder.cComment.setText(comment);
        holder.ctime.setText(ctime);
        holder.cUserName.setText(name);

        try{
            Picasso.get().load(dp).placeholder(R.drawable.avatar).into(holder.dp);
        }
        catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return aboutDriversList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView dp;
        TextView cUserName,ctime,cComment;

        public MyHolder(View itemView) {
            super(itemView);

            dp=itemView.findViewById(R.id.avatarIVabout_driver_comments);
            cUserName=itemView.findViewById(R.id.nameTVabout_driver_comments);
            ctime=itemView.findViewById(R.id.timeTVabout_driver_comments);
            cComment=itemView.findViewById(R.id.commentTVabout_driver_comments);
        }
    }
}
