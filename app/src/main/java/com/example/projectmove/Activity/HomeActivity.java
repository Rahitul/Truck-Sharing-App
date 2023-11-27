package com.example.projectmove.Activity;

import static com.example.projectmove.Utilis.Class.Constants.TOPIC;
import static com.example.projectmove.Utilis.Class.Constants.TOPIC_POST_CUSTOMER;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.example.projectmove.Fragment.HistoryFragment;
import com.example.projectmove.Fragment.ListsFragment;
import com.example.projectmove.Fragment.NotificationsFragment;
import com.example.projectmove.Fragment.PostsFragment;
import com.example.projectmove.Fragment.ProfileFragment;
import com.example.projectmove.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ListsFragment listsFragment = new ListsFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    PostsFragment postsFragment=new PostsFragment();
    NotificationsFragment notificationsFragment=new NotificationsFragment();

    LinearLayout cardViewInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        //Subscribe for notification
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/sendOffers");
        //FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_POST_CUSTOMER);




        bottomNavigationView  = findViewById(R.id.bottom_navigation);
        cardViewInternet=findViewById(R.id.linearLayoutInternet);

        /* // Get the connectivity manager
        ConnectivityManager cm = (ConnectivityManager) getSystemService(HomeActivity.CONNECTIVITY_SERVICE);

// Check for internet connectivity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

// Show the button CardView if there is no internet connection
        if (!isConnected) {
            cardViewInternet.setVisibility(View.VISIBLE);
        } else {
            cardViewInternet.setVisibility(View.GONE);
        }*/


        getSupportFragmentManager().beginTransaction().replace(R.id.container,listsFragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.lists:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,listsFragment).commit();
                        return true;

                    case R.id.posts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,postsFragment).commit();
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,historyFragment).commit();
                        return true;
                    case R.id.notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationsFragment).commit();
                        return true;
                }

                return false;
            }
        });


    }
}