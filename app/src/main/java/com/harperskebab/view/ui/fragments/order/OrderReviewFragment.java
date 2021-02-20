package com.harperskebab.view.ui.fragments.order;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.FragmentOrderReviewBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.OrderReviewViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.io.UnsupportedEncodingException;

public class OrderReviewFragment extends BaseFragment {
    private static final String TAG = "OrderTrackFragment";

    private FragmentOrderReviewBinding binding;
    private UserViewModel userViewModel;
    private OrderReviewViewModel orderReviewViewModel;

    private int containerID;
    private String orderID;

    public OrderReviewFragment() {
    }

    public static OrderReviewFragment newInstance(int containerID, String orderID) {

        OrderReviewFragment fragment = new OrderReviewFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString(Constant.Data.ORDER_ID, orderID);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            orderID = getArguments().getString(Constant.Data.ORDER_ID);
        }
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        orderReviewViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderReviewViewModel.class);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getWriteAReview());
        binding = FragmentOrderReviewBinding.inflate(inflater, container, false);

        binding.ratingBar.setProgress(0);

        binding.buttonSubmit.setOnClickListener(this::onClick);

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

        if (v.getId() == binding.buttonSubmit.getId()) {

            int rating = (int) binding.ratingBar.getRating();
            String comment = binding.editTextComment.getText().toString();

            if (comment.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseWriteYourReview());
            } else {
                String base64Comment = "";
                byte[] data;
                try {
                    data = comment.getBytes("UTF-8");
                    base64Comment = Base64.encodeToString(data, Base64.DEFAULT);
                    Log.i("Base 64 ", base64Comment);
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();

                }
                orderReviewViewModel.updateReview(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderID, userViewModel.getSignInResponse().getValue().getCustomerId(),
                        base64Comment + rating, "", "", "", comment, new NetworkOperations(true));

                orderReviewViewModel.getUpdateReviewResponse().observe(this, updateReviewResponse -> {
                    if (updateReviewResponse != null) {
                        showMessage("Alert", updateReviewResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                            goBack();
                            dialogInterface.dismiss();
                        }, dialogInterface -> {
                            dialogInterface.dismiss();
                        });
                        orderReviewViewModel.getUpdateReviewResponse().removeObservers(this);
                    }
                });
            }

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

    public static String getTAG() {
        return TAG;
    }

}
