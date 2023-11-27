package com.example.admin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Class.AllUsers;
import com.example.admin.Class.CustomersJobs;
import com.example.admin.R;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllUsers extends RecyclerView.Adapter<AdapterAllUsers.MyHolder> {

    Context context;
    List<AllUsers> allUsersList;
    FirebaseAuth auth;
    FirebaseUser user;

    public AdapterAllUsers(Context context, List<AllUsers> allUsersList){
        this.context=context;
        this.allUsersList=allUsersList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_all_users,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {


        String address,imageUrl,nationalIdCardNo,phone,uid,username,isVerified;
        address=allUsersList.get(position).getAddress();
        imageUrl=allUsersList.get(position).getImageUrl();
        nationalIdCardNo=allUsersList.get(position).getNationalIdCardNo();
        phone=allUsersList.get(position).getPhone();
        uid=allUsersList.get(position).getUid();
        username=allUsersList.get(position).getUsername();
        isVerified=allUsersList.get(position).getIsVerified();

        holder.userName.setText(username);
        holder.userNid.setText(nationalIdCardNo);
        holder.userPhone.setText(phone);
        holder.userAddress.setText(address);
        holder.userId.setText(uid);
        holder.userVerifyStatus.setText(isVerified);

        try{
            Picasso.get().load(imageUrl).placeholder(R.drawable.avatar).into(holder.userDp);
        }
        catch (Exception e){

        }


        holder.doVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Verify");
                builder.setMessage("Do you want to verify?");
                builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DatabaseReference refVerify= FirebaseDatabase.getInstance().getReference("Users").child(phone);

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isVerified","Verified");

                        refVerify.updateChildren(hashMap);

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
        });



        holder.doDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(phone);
                        ref.removeValue();

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
        });


    }

    @Override
    public int getItemCount() {
        return allUsersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView userDp;
        TextView userAddress,userPhone,userNid,userId,userName,userVerifyStatus;
        Button doVerify,doDelete;

        public MyHolder(View itemView) {
            super(itemView);

            userDp=itemView.findViewById(R.id.userDp);
            userAddress=itemView.findViewById(R.id.userAddress);
            userPhone=itemView.findViewById(R.id.userPhone);
            userNid=itemView.findViewById(R.id.userNationaNID);
            userId=itemView.findViewById(R.id.userId);
            userName=itemView.findViewById(R.id.userName);
            userVerifyStatus=itemView.findViewById(R.id.verifyStatus);
            doVerify=itemView.findViewById(R.id.doVerify);
            doDelete=itemView.findViewById(R.id.doDelete);
        }
    }
}
