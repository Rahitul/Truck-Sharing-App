package com.example.projectmove.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class PostAmbulanceActivity extends AppCompatActivity {

    ImageView back;
    EditText name,expectedPrice,location,vehicleNumber;
    AutoCompleteTextView hospitalName;
    Spinner ambulanceVehicleModel;
    String aDriverName,aExpectedPrice,aLocation,aHospitalName,aVehicleModel,
            aVehicleNumber,uid,phone,uDp,uName,timeStamp,isAccepted="Not Accepted";
    ProgressDialog pd;

    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference myRef;
    FirebaseDatabase database;

    LinearLayout ambulanceAddPostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ambulance);

        back=findViewById(R.id.backAmbulancePost);
        name=findViewById(R.id.cNameETAmbulancePost);
        expectedPrice=findViewById(R.id.cExpectedPriceETAmbulancePost);
        location=findViewById(R.id.et_sourceAmbulancePost);
        hospitalName=findViewById(R.id.et_destinationAmbulancePost);
        ambulanceVehicleModel=findViewById(R.id.AmbulancePost_spinner_Vehicle_Model);
        ambulanceAddPostBtn=findViewById(R.id.ambulanceAddPostBtn);
        vehicleNumber=findViewById(R.id.NumberPlateAmbulancePost);

        hospitalName.setThreshold(2);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,HospitalName);
        hospitalName.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item);
       // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ambulanceVehicleModel.setAdapter(adapter2);

        ambulanceVehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Do something when an item is selected
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), selectedItem + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing when nothing is selected
            }
        });


        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Users");

        uid=user.getUid();
        phone=user.getPhoneNumber();



        myRef=FirebaseDatabase.getInstance().getReference("Users");
        Query query=myRef.orderByChild("phone").equalTo(phone);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    uName= ""+ds.child("username").getValue();
                    phone= ""+ds.child("phone").getValue();
                    uDp= ""+ds.child("imageUrl").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ambulanceAddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd=new ProgressDialog(PostAmbulanceActivity.this);
                pd.setMessage("Adding Post");
                pd.show();

                aDriverName=name.getText().toString().trim();
                aExpectedPrice=expectedPrice.getText().toString().trim();
                aLocation=location.getText().toString().trim();
                aHospitalName=hospitalName.getText().toString().trim();
                aVehicleModel=ambulanceVehicleModel.getSelectedItem().toString().trim();
                long currentTime=System.currentTimeMillis();
                timeStamp=String .valueOf(currentTime);
                aVehicleNumber=vehicleNumber.getText().toString().trim();

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("AmbulancePosts");

                HashMap<String,Object> hashMap=new HashMap<>();

                hashMap.put("pid",timeStamp);
                hashMap.put("uid",uid);
                hashMap.put("isAccepted",isAccepted);
                hashMap.put("uPhone",phone);
                hashMap.put("pVehicleModel",aVehicleModel);
                hashMap.put("pPrice",aExpectedPrice);
                hashMap.put("pLocation",aLocation);
                hashMap.put("pDestination",aHospitalName);
                hashMap.put("pName",aDriverName);
                hashMap.put("userName",uName);
                hashMap.put("pTime",timeStamp);
                hashMap.put("pDp",uDp);
                hashMap.put("vehicleNumber",aVehicleNumber);
                hashMap.put("pComments","0");



                ref.child(timeStamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pd.dismiss();
                                Toast.makeText(PostAmbulanceActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(PostAmbulanceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    private void addAmbulancePost() {


    }

    private static final String[] HospitalName= new String[]{"LabAid Hospital","Shomorita Hospital"};
}