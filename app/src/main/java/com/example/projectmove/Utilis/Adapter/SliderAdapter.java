package com.example.projectmove.Utilis.Adapter;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.projectmove.R;

public class SliderAdapter extends PagerAdapter {

    int[] images;
    LayoutInflater layoutInflater;
    Context context;

    //alt + ins to create cons


    public SliderAdapter(int[] images, Context context) {
        this.images = images;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //View myImageLayout = layoutInflater.inflate(R.layout.slide_images,container, false);
        //ImageView imageview = myImageLayout.findViewById(R.id.imageview);

        View myImageLayout=layoutInflater.inflate(com.example.projectmove.R.layout.slide_images,container,false);
        ImageView imageView =myImageLayout.findViewById(com.example.projectmove.R.id.imageview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(context.getDrawable(images[position]));
        }else {
            imageView.setImageDrawable(context.getResources().getDrawable(images[position]));
        }

        container.addView(myImageLayout);

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals(object);
    }
}
