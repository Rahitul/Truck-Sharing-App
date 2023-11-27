package com.example.projectmove.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView dp;
    EditText name,Address,nationalIdNo,vehicleModel;
    TextView submitModel,submit;
    DatabaseReference vehicleModelRef;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        vehicleModelRef= FirebaseDatabase.getInstance().getReference("VehicleModel");

        dp=findViewById(R.id.EPImageViewId);
        name=findViewById(R.id.EPenterUserName);
        Address=findViewById(R.id.EPenterAddress);
        nationalIdNo=findViewById(R.id.EPenterNationalIdCardNo);
        vehicleModel=findViewById(R.id.EPenterVehiclesModel);
        submitModel=findViewById(R.id.EPAddVehiclesModel);
        submit=findViewById(R.id.EPcontinue_btn);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);
        showUserInfo();


        submitModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVehiclesModel();
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                if (selectedImage != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getCurrentUser().getPhoneNumber());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String imageUrl = uri.toString();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String uid=auth.getCurrentUser().getUid();
                                        String username=name.getText().toString();
                                        String address=Address.getText().toString();
                                        String nationalIdCardNo=nationalIdNo.getText().toString();
                                        String isVerified="Not Verified";



                                        HashMap<String,Object> hashMap=new HashMap<>();
                                        hashMap.put("imageUrl",imageUrl);
                                        hashMap.put("uid",uid);
                                        hashMap.put("username",username);
                                        hashMap.put("address",address);
                                        hashMap.put("nationalIdCardNo",nationalIdCardNo);


                                        database.getReference()
                                                .child("Users")
                                                .child(phone)
                                                .updateChildren(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                                                        Toast.makeText(EditProfileActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                    }
                                });
                            }
                        }
                    });
                } else {
                }
            }
        });



    }

    private void addVehiclesModel() {

        String VehicleModel=vehicleModel.getText().toString().trim();
        vehicleModelRef.child(auth.getCurrentUser().getPhoneNumber()).push().setValue(VehicleModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        vehicleModel.setText("");
                        Toast.makeText(EditProfileActivity.this, "মডেলঃ "+VehicleModel, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                Uri uri = data.getData(); // filepath
                FirebaseStorage storage = FirebaseStorage.getInstance();
                long time = new Date().getTime();
                StorageReference reference = storage.getReference().child("Profiles").child(time + "");
                reference.putFile(uri);
                dp.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }

    }

    private void showUserInfo() {

        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");

        String phone = auth.getCurrentUser().getPhoneNumber();

        myRef.child(phone).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);

                if(value!=null){


                    Picasso.get().load(value).into(dp);
                }



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
                        String mName=map.get("username").toString();
                        name.setText(mName);
                    }

                    if(map.get("address")!=null){
                        String mName=map.get("address").toString();
                        Address.setText(mName);
                    }

                    if(map.get("nationalIdCardNo")!=null){
                        String mName=map.get("nationalIdCardNo").toString();
                        nationalIdNo.setText(mName);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}