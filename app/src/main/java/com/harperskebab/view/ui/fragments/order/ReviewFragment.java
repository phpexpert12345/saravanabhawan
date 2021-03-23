package com.harperskebab.view.ui.fragments.order;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentReviewBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.ReviewAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.ReviewViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class ReviewFragment extends BaseFragment {
    private static final String TAG = "ReviewFragment";

    private FragmentReviewBinding binding;
    private UserViewModel userViewModel;
    private ReviewViewModel reviewViewModel;
    private int containerID;

    public ReviewFragment() {
    }

    public static ReviewFragment newInstance(int containerID) {

        ReviewFragment fragment = new ReviewFragment();
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
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        reviewViewModel = ViewModelFactory.getInstance(getActivity()).create(ReviewViewModel.class);
        reviewViewModel.getReviews(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, userViewModel.getSignInResponse().getValue().getCustomerId(), new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getReview());
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        binding.emptyView.linearEmpty.setVisibility(View.GONE);
        binding.recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getActivity()));

        reviewViewModel.getReviewsResponse().observe(this, restaurantReviews -> {

            if (restaurantReviews != null) {

                if (restaurantReviews.getSuccess().equals(0L)) {
                    if (restaurantReviews.getReview().getRestaurantReviews().size() == 0) {

                        binding.linearLayoutContent.setVisibility(View.GONE);
                        binding.emptyView.linearEmpty.setVisibility(View.VISIBLE);
                        binding.emptyView.txtEmpty.setText(restaurantReviews.getSuccess_msg());
                        binding.emptyView.imgEmpty.setImageResource(R.drawable.ic_review_empty);

                    } else {
                        binding.linearLayoutContent.setVisibility(View.VISIBLE);
                        binding.emptyView.linearEmpty.setVisibility(View.GONE);
                        ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), restaurantReviews.getReview().getRestaurantReviews());
                        binding.recyclerViewReviews.setAdapter(reviewAdapter);
                        reviewViewModel.getReviewsResponse().removeObservers(this);
                    }
                } else {

                    binding.linearLayoutContent.setVisibility(View.GONE);
                    binding.emptyView.linearEmpty.setVisibility(View.VISIBLE);
                    binding.emptyView.txtEmpty.setVisibility(View.VISIBLE);
                    binding.emptyView.imgEmpty.setImageResource(R.drawable.ic_review_empty);
                    binding.emptyView.txtEmpty.setText(restaurantReviews.getSuccess_msg());

                }
            }
            else{
//                binding.linearLayoutContent.setVisibility(View.GONE);
//                binding.emptyView.linearEmpty.setVisibility(View.VISIBLE);
//                binding.emptyView.imgEmpty.setImageResource(R.drawable.ic_review_empty);
            }

        });

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
