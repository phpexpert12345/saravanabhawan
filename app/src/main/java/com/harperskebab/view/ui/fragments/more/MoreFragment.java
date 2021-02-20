package com.harperskebab.view.ui.fragments.more;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.harperskebab.databinding.FragmentMoreBinding;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.fragments.BaseFragment;

public class MoreFragment extends BaseFragment {
    private static final String TAG = "MoreFragment";

    private FragmentMoreBinding binding;

    private int containerID;

    public MoreFragment() {
    }

    public static MoreFragment newInstance(int containerID) {

        MoreFragment fragment = new MoreFragment();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("");
        binding = FragmentMoreBinding.inflate(inflater, container, false);
binding.textViewReviews.setText(languageViewModel.getLanguageResponse().getValue().getRestaurantReview());
binding.textViewInfo.setText(languageViewModel.getLanguageResponse().getValue().getInfo());
binding.textViewOpeningHours.setText(languageViewModel.getLanguageResponse().getValue().getOpeningHours());
binding.textViewBranches.setText(languageViewModel.getLanguageResponse().getValue().getBranches());
binding.textViewDeliveryArea.setText(languageViewModel.getLanguageResponse().getValue().getDeliveryArea());
binding.textViewBookATable.setText(languageViewModel.getLanguageResponse().getValue().getBookATable());
binding.textViewContactUs.setText(languageViewModel.getLanguageResponse().getValue().getContactUs());
        binding.textViewGallery.setText(languageViewModel.getLanguageResponse().getValue().getGallery());
        binding.textView.setText(languageViewModel.getLanguageResponse().getValue().getOffer());
        binding.cardViewInfo.setOnClickListener(this::onClick);
        binding.cardViewReviews.setOnClickListener(this::onClick);
        binding.cardViewOpeningHours.setOnClickListener(this::onClick);
        binding.cardViewBranches.setOnClickListener(this::onClick);
        binding.cardViewDeliveryArea.setOnClickListener(this::onClick);
        binding.cardViewBookATable.setOnClickListener(this::onClick);
        binding.cardViewContactUs.setOnClickListener(this::onClick);
        binding.cardViewGallery.setOnClickListener(this::onClick);
        binding.cardViewOffers.setOnClickListener(this::onClick);

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
        if (v.getId() == binding.cardViewInfo.getId()) {
            initiateAboutFragment();
        } else if (v.getId() == binding.cardViewReviews.getId()) {
            initiateRestaurantReviewFragment();
        } else if (v.getId() == binding.cardViewOpeningHours.getId()) {
            initiateOpeningHourFragment();
        } else if (v.getId() == binding.cardViewBranches.getId()) {
            initiateBranchesFragment();
        } else if (v.getId() == binding.cardViewDeliveryArea.getId()) {
            initiateDeliveryAreaFragment();
        } else if (v.getId() == binding.cardViewBookATable.getId()) {
            initiateBookATableFragment();
        } else if (v.getId() == binding.cardViewContactUs.getId()) {
            initiateContactUsFragment();
        } else if (v.getId() == binding.cardViewGallery.getId()) {
            initiateGalleryFragment();
        } else if (v.getId() == binding.cardViewOffers.getId()) {
            initiateOfferFragment();
        }
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

    private void initiateAboutFragment() {
        AboutFragment aboutFragment = AboutFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, aboutFragment);
        transaction.addToBackStack(AboutFragment.getTAG());
        transaction.commit();
    }

    private void initiateRestaurantReviewFragment() {
        RestaurantReviewFragment restaurantReviewFragment = RestaurantReviewFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, restaurantReviewFragment);
        transaction.addToBackStack(RestaurantReviewFragment.getTAG());
        transaction.commit();
    }

    private void initiateOpeningHourFragment() {
        OpeningHourFragment openingHourFragment = OpeningHourFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, openingHourFragment);
        transaction.addToBackStack(OpeningHourFragment.getTAG());
        transaction.commit();
    }

    private void initiateBranchesFragment() {
        BranchesFragment branchesFragment = BranchesFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, branchesFragment);
        transaction.addToBackStack(BranchesFragment.getTAG());
        transaction.commit();
    }

    private void initiateDeliveryAreaFragment() {
        DeliveryAreaFragment deliveryAreaFragment = DeliveryAreaFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, deliveryAreaFragment);
        transaction.addToBackStack(DeliveryAreaFragment.getTAG());
        transaction.commit();
    }

    private void initiateBookATableFragment() {
        BookATableFragment bookATableFragment = BookATableFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, bookATableFragment);
        transaction.addToBackStack(BookATableFragment.getTAG());
        transaction.commit();
    }

    private void initiateContactUsFragment() {
        ContactUsFragment contactUsFragment = ContactUsFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, contactUsFragment);
        transaction.addToBackStack(ContactUsFragment.getTAG());
        transaction.commit();
    }

    private void initiateGalleryFragment() {
        GalleryFragment galleryFragment = GalleryFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, galleryFragment);
        transaction.addToBackStack(GalleryFragment.getTAG());
        transaction.commit();
    }

    private void initiateOfferFragment() {
        OfferFragment offerFragment = OfferFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, offerFragment);
        transaction.addToBackStack(OfferFragment.getTAG());
        transaction.commit();
    }


    public static String getTAG() {
        return TAG;
    }

}
