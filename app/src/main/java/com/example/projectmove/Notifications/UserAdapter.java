package com.example.projectmove.Notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectmove.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<TestUser> userList;

    public UserAdapter(Context context, List<TestUser> userList){
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_users, parent, false);
       return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        TestUser user = userList.get(position);
        holder.textViewEmail.setText(user.phoneMessaging);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEmail;

        public UserViewHolder(View itemView) {
            super(itemView);

            textViewEmail = itemView.findViewById(R.id.textViewEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, SendNotificationActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
