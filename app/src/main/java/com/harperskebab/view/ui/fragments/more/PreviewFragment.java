package com.harperskebab.view.ui.fragments.more;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FragmentPreviewBinding;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.fragments.BaseFragment;

public class PreviewFragment extends BaseFragment {
    private static final String TAG = "GalleryFragment";

    private FragmentPreviewBinding binding;

    private int containerID;
    private String foodImageLink;

    public PreviewFragment() {
    }

    public static PreviewFragment newInstance(int containerID, String foodImageLink) {

        PreviewFragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString(Constant.Data.FOOD_IMAGE_LINK, foodImageLink);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            foodImageLink = getArguments().getString(Constant.Data.FOOD_IMAGE_LINK);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getPreview());
        binding = FragmentPreviewBinding.inflate(inflater, container, false);

        Glide.with(getActivity()).load(foodImageLink).placeholder(R.drawable.app_logo).into(binding.imageViewFood);

        return binding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

    }

    @Override
    public boolean onBackPressed(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                goBack();
                return true;
            }
        }
        return false;
    }

    public static String getTAG() {
        return TAG;
    }

}