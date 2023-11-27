package com.example.projectmove.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmove.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    private EditText mVerify;
    TextView textView7;
    ProgressBar progressBar7;
    private String phoneNumber;
    private TextView button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        mAuth = FirebaseAuth.getInstance();
        progressBar7 = findViewById(R.id.progressBar7);
        textView7 = findViewById(R.id.textView7);
        textView7.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyOtpActivity.this, MainActivity.class);
            startActivity(intent);
        });
        mVerify = findViewById(R.id.editText);
        button5= findViewById(R.id.button5);
        final String phonenumber = getIntent().getStringExtra("phonenumber");

        if (phonenumber.isEmpty() || phonenumber.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        } else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+88" + phonenumber, 60, TimeUnit.SECONDS, VerifyOtpActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            signInUser(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            Toast.makeText(VerifyOtpActivity.this, "Varification Failes", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(verificationId, forceResendingToken);

                            button5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String verificationCode = mVerify.getText().toString();
                                    if (verificationId.isEmpty()) return;
                                    //create a credential
                                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
                                    signInUser(credential);
                                }
                            });
                        }
                    }
            );
        }
    }

    private void signInUser(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(VerifyOtpActivity.this, ProfileActivity.class));
                            finish();

                        } else {
//                             Log.d(TAG, "onComplete:"+task.getException().getLocalizedMessage());
                        }
                    }
                });

    }


    }

