package com.example.projectmove.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmove.R;
import com.example.projectmove.Utilis.Adapter.AdapterDriversJobs;
import com.example.projectmove.Utilis.Class.DriversJobs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ThereProfileActivity extends AppCompatActivity {
    List<DriversJobs> driversJobsList;
    AdapterDriversJobs adapterDriversJobs;
    String uid;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);
        firebaseAuth=FirebaseAuth.getInstance();

        checkUserStatus();
    }

    private void checkUserStatus(){

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){



        }

        else{
            startActivity(new Intent((getApplicationContext()),MainActivity.class));
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}