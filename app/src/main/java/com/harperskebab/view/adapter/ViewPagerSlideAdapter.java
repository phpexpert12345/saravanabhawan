package com.harperskebab.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.ViewPagerSlideListItemRowBinding;
import com.harperskebab.model.FrontBanner;

import java.util.List;


public class ViewPagerSlideAdapter extends PagerAdapter {

    private Context context;
    List<FrontBanner> frontBanners;

    public ViewPagerSlideAdapter(Context context, List<FrontBanner> frontBanners) {
        this.context = context;
        this.frontBanners = frontBanners;
    }

    @Override
    public int getCount() {
        return frontBanners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ViewPagerSlideListItemRowBinding viewPagerSlideListRowBinding = ViewPagerSlideListItemRowBinding.inflate(LayoutInflater.from(container.getContext()));

        Glide.with(context)
                .load(frontBanners.get(position).getAppBannerImg())
                .placeholder(R.drawable.app_logo)
                .into(viewPagerSlideListRowBinding.imgshopImage);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(viewPagerSlideListRowBinding.getRoot(), position);

        return viewPagerSlideListRowBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);

    }
}