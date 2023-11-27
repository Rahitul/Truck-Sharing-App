package com.example.projectmove.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterAmbulanceJobs;
import com.example.projectmove.Utilis.Adapter.AdapterCustomersJobs;
import com.example.projectmove.Utilis.Class.AmbulanceJobs;
import com.example.projectmove.Utilis.Class.CustomersJobs;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceActivity extends AppCompatActivity {

    ExtendedFloatingActionButton fabAmbulance;
    RecyclerView recyclerView;

    List<AmbulanceJobs> ambulanceJobLists;
    AdapterAmbulanceJobs adapterAmbulanceJobs;

    AutoCompleteTextView searchLocation,searchHospital;
    ImageView locationDropDown,hospitalDropDown;

    LottieAnimationView progressBar;

    ImageView back;

    LinearLayout vehicleTypeLayout;
    ImageView searchAmbulance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        back=findViewById(R.id.backAmbulance);
        recyclerView=findViewById(R.id.ambulancePostsRecyclerView);
        searchAmbulance=findViewById(R.id.searchAmbulance);

        progressBar=findViewById(R.id.progressBarAmbulance);

        progressBar.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fabAmbulance=findViewById(R.id.fabAmbulance);
        fabAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AmbulanceActivity.this,PostAmbulanceActivity.class);
                startActivity(intent);
            }
        });



        searchAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchDialog();
            }
        });




        //scrollAnimation
        final int THRESHOLD = 20; // set a threshold to control the scroll distance
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fabAmbulance.getLayoutParams();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDist = 0;
            boolean isVisible = true;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isVisible && scrollDist > THRESHOLD) {
                    // hide the button if it's visible and scroll distance exceeds threshold
                    isVisible = false;
                    params.setMargins(0, 0, 0, -fabAmbulance.getHeight());
                    fabAmbulance.setLayoutParams(params);
                    fabAmbulance.setVisibility(View.GONE);
                } else if (!isVisible && scrollDist < -THRESHOLD) {
                    // show the button if it's hidden and scroll distance is less than negative threshold
                    isVisible = true;
                    params.setMargins(0, 0, 0, 0);
                    fabAmbulance.setLayoutParams(params);
                    fabAmbulance.setVisibility(View.VISIBLE);
                }
                if((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                    // calculate the scroll distance based on the scroll direction
                    scrollDist += dy;
                }
            }
        });






        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        ambulanceJobLists=new ArrayList<>();

        loadAmbulancePosts();
    }

    private void showSearchDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_filter_bottom_sheetlayout);

        searchHospital=dialog.findViewById(R.id.searchDestination);
        searchLocation=dialog.findViewById(R.id.searchLocation);
        searchHospital.setHint("হসপিটালের নাম দিন");

        ImageView cancel=dialog.findViewById(R.id.cancel);
        TextView search=dialog.findViewById(R.id.search);

        vehicleTypeLayout=dialog.findViewById(R.id.vehicleTypeLayout);
        vehicleTypeLayout.setVisibility(View.GONE);

        locationDropDown=dialog.findViewById(R.id.locationDropDown);
        hospitalDropDown=dialog.findViewById(R.id.destinationDropDown);

        locationDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation.showDropDown();
            }
        });

        hospitalDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHospital.showDropDown();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        searchLocation.setThreshold(2);
        searchHospital.setThreshold(1);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,location);
        searchLocation.setAdapter(adapter);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,destination);
        searchHospital.setAdapter(adapter1);


        searchHospital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterLocation(editable.toString());
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void filterLocation(String text) {

        ArrayList<AmbulanceJobs> filteredList = new ArrayList<>();

        for (AmbulanceJobs item : ambulanceJobLists) {
            if (item.getpLocation().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapterAmbulanceJobs.filterList(filteredList);


    }

    private void filter(String text) {

        ArrayList<AmbulanceJobs> filteredList = new ArrayList<>();

        for (AmbulanceJobs item : ambulanceJobLists) {
            if (item.getpDestination().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapterAmbulanceJobs.filterList(filteredList);


    }

    private void loadAmbulancePosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("AmbulancePosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ambulanceJobLists.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    AmbulanceJobs ambulanceJobs =ds.getValue(AmbulanceJobs.class);

                    ambulanceJobLists.add(ambulanceJobs);

                    adapterAmbulanceJobs=new AdapterAmbulanceJobs(getApplicationContext(),ambulanceJobLists);
                    recyclerView.setAdapter(adapterAmbulanceJobs);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static final String[] location= new String[]{"Nellore","Dhanmondi"};
    private static final String[] destination= new String[]{"LabAid Hospital","Shomorita Hospital"};
}