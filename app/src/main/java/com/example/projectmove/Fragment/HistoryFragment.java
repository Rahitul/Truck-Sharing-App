package com.example.projectmove.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.projectmove.Utilis.Adapter.MyViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.example.projectmove.R;


public class HistoryFragment extends Fragment {

    View myFragment;

    ViewPager viewPager;
    TabLayout tabLayout;


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment getInstance()    {
        return new HistoryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_history, container, false);

        viewPager = myFragment.findViewById(R.id.viewPager);
        tabLayout = myFragment.findViewById(R.id.tabLayout);

        return myFragment;
    }

    //Call onActivity Create method


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new HistoryMainFragment(), "হিস্টোরি");
        adapter.addFragment(new HistoryRunningFragment(), "রানিং ট্রিপ");

        viewPager.setAdapter(adapter);
    }
}