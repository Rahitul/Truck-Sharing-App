package com.example.admin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.admin.R;

public class HomeActivity extends AppCompatActivity {

    Button sendOffers,seeAllUsers,seePostsOfDriver,seePostsOfCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sendOffers=findViewById(R.id.sendOfferMessage);
        seeAllUsers=findViewById(R.id.seeAllUsers);
        seePostsOfCustomer=findViewById(R.id.seePostsOfCustomer);
        seePostsOfDriver=findViewById(R.id.seePostsOfDriver);

        sendOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,SendOffersActivity.class);
                startActivity(intent);
            }
        });

        seePostsOfCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,SeePostsOfCustomer.class);
                startActivity(intent);
            }
        });

        seePostsOfDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,SeePostsOfDriver.class);
                startActivity(intent);
            }
        });

        seeAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,SeeAllUsersActivity.class);
                startActivity(intent);
            }
        });

    }
    }