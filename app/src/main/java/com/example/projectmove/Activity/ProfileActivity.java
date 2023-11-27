package com.example.projectmove.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Class.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    CircleImageView imageView;
    EditText usernameET,addressET,nationalIdCardNoET,enterVehiclesModel;
    TextView continueBtn,addVehiclesModel;

    DatabaseReference myRef,vehicleModelRef;
    String vehicleModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = findViewById(R.id.ImageViewId);
        continueBtn = findViewById(R.id.continue_btn);
        usernameET=findViewById(R.id.enterUserName);
        addressET=findViewById(R.id.enterAddress);
        nationalIdCardNoET=findViewById(R.id.enterNationalIdCardNo);

        enterVehiclesModel=findViewById(R.id.enterVehiclesModel);
        addVehiclesModel=findViewById(R.id.addVehiclesModel);

        vehicleModelRef=FirebaseDatabase.getInstance().getReference("VehicleModel");



        addVehiclesModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVehiclesModel();
            }
        });


        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        //addRatingValue
        DatabaseReference addRating=FirebaseDatabase.getInstance().getReference("Rating").child(auth.getCurrentUser().getUid());

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("value","0.0");
        hashMap.put("count","0.0");
        hashMap.put("addition","0.0");


        addRating.setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(ProfileActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



        //add__SelectAVehicleModel
        String phone = auth.getCurrentUser().getPhoneNumber();
        vehicleModelRef.child(phone).push().setValue("গাড়ির মডেল সিলেক্ট করুন")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                    }
                });

        //showUserInfo();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                        String username=usernameET.getText().toString();
                                        String address=addressET.getText().toString();
                                        String nationalIdCardNo=nationalIdCardNoET.getText().toString();
                                        String isVerified="Not Verified";

                                        UserHelperClass addNewUser = new UserHelperClass(imageUrl,username,address,nationalIdCardNo,phone,uid,isVerified);

                                        database.getReference()
                                                .child("Users")
                                                .child(phone)
                                                .setValue(addNewUser)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                                                        Toast.makeText(ProfileActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
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

                    String phone = auth.getCurrentUser().getPhoneNumber();

                    UserHelperClass addNewUser = new UserHelperClass("No Image","No Username","No Address ","No NationalIdNo","No Phone","No uid","Nothing");

                    database.getReference()
                            .child("Users")
                            .child(phone)
                            .setValue(addNewUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                                    Toast.makeText(ProfileActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }

            }
        });

    }

    private void addVehiclesModel() {

        vehicleModel=enterVehiclesModel.getText().toString();
        vehicleModelRef.child(auth.getCurrentUser().getPhoneNumber()).push().setValue(vehicleModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        enterVehiclesModel.setText("");
                        Toast.makeText(ProfileActivity.this, "মডেলঃ "+vehicleModel, Toast.LENGTH_SHORT).show();
                    }
                });
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


                    Picasso.get().load(value).into(imageView);
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
                        usernameET.setText(mName);
                    }

                    if(map.get("address")!=null){
                        String mName=map.get("address").toString();
                        addressET.setText(mName);
                    }

                    if(map.get("nationalIdCardNo")!=null){
                        String mName=map.get("nationalIdCardNo").toString();
                        nationalIdCardNoET.setText(mName);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("image", filePath);
                                    database.getReference().child("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                imageView.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }

    }
}