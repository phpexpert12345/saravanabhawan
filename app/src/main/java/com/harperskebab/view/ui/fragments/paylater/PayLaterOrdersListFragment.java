package com.harperskebab.view.ui.fragments.paylater;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentPayLaterOrdersListBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.PayLaterOrderAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.harperskebab.viewmodel.paylater.PayLaterCartViewModel;
import com.harperskebab.viewmodel.paylater.PayLaterOrderViewModel;

public class PayLaterOrdersListFragment extends BaseFragment {
    private static final String TAG = "PayLaterOrdersListFragm";

    private FragmentPayLaterOrdersListBinding binding;

    private UserViewModel userViewModel;
    private PayLaterOrderViewModel payLaterOrderViewModel;
    private PayLaterCartViewModel payLaterCartViewModel;
    private PayLaterOrderAdapter payLaterOrderAdapter;
    private int containerID;

    public PayLaterOrdersListFragment() {
    }

    public static PayLaterOrdersListFragment newInstance(int containerID) {

        PayLaterOrdersListFragment fragment = new PayLaterOrdersListFragment();
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
        payLaterCartViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterCartViewModel.class);
        payLaterOrderViewModel = ViewModelFactory.getInstance(getActivity()).create(PayLaterOrderViewModel.class);
        payLaterOrderViewModel.getPayLaterOrderList(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, userViewModel.getSignInResponse().getValue().getCustomerId(), new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.pay_later));
        binding = FragmentPayLaterOrdersListBinding.inflate(inflater, container, false);

        binding.recyclerViewPayLaterOrders.setLayoutManager(new LinearLayoutManager(getActivity()));

        payLaterOrderViewModel.getOrderViewResult().observe(this, orderViewResults -> {

            if (orderViewResults != null) {

                payLaterOrderAdapter = new PayLaterOrderAdapter(
                        getActivity(), orderViewResults, onItemClick, onContinueOrder, onPayNow,
                        Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                );
                binding.recyclerViewPayLaterOrders.setAdapter(payLaterOrderAdapter);
                binding.linearLayoutContent.setVisibility(View.VISIBLE);
                binding.emptyView.getRoot().setVisibility(View.GONE);
            }else {
                binding.linearLayoutContent.setVisibility(View.GONE);
                binding.emptyView.getRoot().setVisibility(View.VISIBLE);
                binding.emptyView.textViewErrorMessage.setText("Sorry! You have not placed any order yet");
            }

        });

        return binding.getRoot();
    }

    PayLaterOrderAdapter.OnItemClick onItemClick = (position, orderViewResult) -> {
        initiatePayLaterOrderDetailFragment(orderViewResult.getOrderIdentifyno());
    };

    PayLaterOrderAdapter.OnContinueOrder onContinueOrder = (position, orderViewResult) -> {
        initiatePayLaterOrderDetailFragment(orderViewResult.getOrderIdentifyno());
    };

    PayLaterOrderAdapter.OnPayNow onPayNow = (position, orderViewResult) -> {

        payLaterCartViewModel.getSubTotal().setValue("0.00");
        payLaterCartViewModel.getToPay().setValue(orderViewResult.getOrdPrice());

        initiatePaymentFragment(orderViewResult.getOrderIdentifyno());
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

    }

    private void initiatePayLaterOrderDetailFragment(String orderID) {
        PayLaterOrderDetailFragment payLaterOrderDetailFragment = PayLaterOrderDetailFragment.newInstance(containerID, orderID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, payLaterOrderDetailFragment);
        transaction.addToBackStack(PayLaterOrderDetailFragment.getTAG());
        transaction.commit();
    }

    private void initiatePaymentFragment(String orderID) {
        PayLaterPaymentFragment paymentFragment = PayLaterPaymentFragment.newInstance(containerID, orderID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, paymentFragment);
        transaction.addToBackStack(paymentFragment.getTag());
        transaction.commit();
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
