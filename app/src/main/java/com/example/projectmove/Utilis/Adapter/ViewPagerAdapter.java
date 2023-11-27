package com.example.projectmove.Utilis.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.example.projectmove.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Integer> images;

    public ViewPagerAdapter(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, container, false);
        ImageView imageView = view.findViewById(R.id.image_view);
        imageView.setImageResource(images.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
