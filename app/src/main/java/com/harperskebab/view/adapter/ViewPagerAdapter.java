package com.harperskebab.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.model.SplashBanners;
import com.harperskebab.view.ui.fragments.startup.StartupFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<SplashBanners> images;
    private LayoutInflater inflater;
    private Context context;

    public ViewPagerAdapter(Context context, List<SplashBanners> images){
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position){
        View myImageLayout  = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        Glide.with(context)
                .load(images.get(0).getSplashBannerImg())
                .centerCrop()
                .into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}