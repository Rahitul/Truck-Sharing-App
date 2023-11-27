package com.example.projectmove.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.projectmove.Notifications.TestProfile;
import com.example.projectmove.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    EditText email;
    TextView button5;
    ProgressBar progressBar6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        email = findViewById(R.id.email);
        progressBar6 = findViewById(R.id.progressBar6);
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> {
            progressBar6.setVisibility(View.VISIBLE);
            String mobile = email.getText().toString().trim();
            if (mobile.isEmpty() || mobile.length() < 10){
                Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                progressBar6.setVisibility(View.INVISIBLE);
            }else {
                @SuppressWarnings("UnnecessaryLocalVariable") String phonenumber = mobile;
                Intent intent = new Intent(MainActivity.this, VerifyOtpActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
                progressBar6.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
