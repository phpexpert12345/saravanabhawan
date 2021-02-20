package com.harperskebab.view.ui.fragments.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FragmentStartupSecondBinding;
import com.harperskebab.databinding.FragmentStartupThirdBinding;
import com.harperskebab.view.ui.activities.MapActivity;
import com.harperskebab.viewmodel.SplashViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class StartupThirdFragment extends Fragment {
    private static final String TAG = "StartupThirdFragment";

    private FragmentStartupThirdBinding binding;
    private SplashViewModel splashViewModel;

    public StartupThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel = ViewModelFactory.getInstance(getActivity()).create(SplashViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartupThirdBinding.inflate(inflater, container, false);

        splashViewModel.getSplashBanners().observe(this, splashBanners -> {

            if (splashBanners != null) {
                Glide.with(getActivity())
                        .load(splashBanners.get(2).getSplashBannerImg())
//                        .placeholder(R.drawable.loading)
                        .centerCrop()
                        .into(binding.imageView);
                splashViewModel.getSplashBanners().removeObservers(this);
                binding.layInd.setVisibility(View.VISIBLE);
            }else {
                binding.layInd.setVisibility(View.GONE);
            }

        });
        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapActivity.class));
                getActivity().finish();
//                if (isType){
//                    startActivity(new Intent(getActivity(), MapActivity.class));
//                    getActivity().finish();
//                }else {
//
//                }
            }
        });
        return binding.getRoot();
    }

    public static String getTAG() {
        return TAG;
    }

}