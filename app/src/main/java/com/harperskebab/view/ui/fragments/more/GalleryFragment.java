package com.harperskebab.view.ui.fragments.more;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.harperskebab.databinding.FragmentGalleryBinding;
import com.harperskebab.model.FoodGallery;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.FoodGalleryAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.GalleryViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.List;

public class GalleryFragment extends BaseFragment {
    private static final String TAG = "GalleryFragment";

    private FragmentGalleryBinding binding;
    private GalleryViewModel galleryViewModel;
    private List<FoodGallery> foodGalleries;

    private int containerID;
    String BranchId;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(int containerID) {

        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
        }
        BranchId= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId","2");
        galleryViewModel = ViewModelFactory.getInstance(getActivity()).create(GalleryViewModel.class);
        galleryViewModel.getDeliveryArea(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "1", new NetworkOperations(true),BranchId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getGallery());
        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        binding.recyclerViewFoodGallery.setLayoutManager(gridLayoutManager);

        binding.tabLayoutFoodGallery.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                FoodGalleryAdapter foodGalleryAdapter = new FoodGalleryAdapter(getActivity(), foodGalleries.get(tab.getPosition()).getGalleryPhoto(), onItemClick);
                binding.recyclerViewFoodGallery.setAdapter(foodGalleryAdapter);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        galleryViewModel.getFoodGalleries().observe(this, foodGalleries -> {

            if (foodGalleries != null) {

                this.foodGalleries = foodGalleries;

                for (int i = 0; i < this.foodGalleries.size(); i++) {
                    FoodGallery foodGallery = this.foodGalleries.get(i);
                    TabLayout.Tab firstTab = binding.tabLayoutFoodGallery.newTab();
                    firstTab.setText(foodGallery.getTabName());
                    binding.tabLayoutFoodGallery.addTab(firstTab, i);
                }
                binding.tabLayoutFoodGallery.selectTab(binding.tabLayoutFoodGallery.getTabAt(0));
                galleryViewModel.getFoodGalleries().removeObservers(this);

            }

        });

        return binding.getRoot();
    }

    private FoodGalleryAdapter.OnItemClick onItemClick = (position, photo) -> initiateFoodItemFragment(photo.getFoodPhoto());

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

    private void initiateFoodItemFragment(String foodImageLink) {
        PreviewFragment previewFragment = PreviewFragment.newInstance(containerID, foodImageLink);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(containerID, previewFragment);
        transaction.addToBackStack(PreviewFragment.getTAG());
        transaction.commit();
    }


}