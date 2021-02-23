package com.harperskebab.view.ui.fragments.order;

import android.content.Intent;
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
import com.harperskebab.databinding.FragmentMyOrdersListBinding;
import com.harperskebab.model.Order;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.OrderAdapter;
import com.harperskebab.view.ui.activities.CartActivity;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.view.ui.activities.OrderDetailActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.OrderCancelViewModel;
import com.harperskebab.viewmodel.OrderViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class MyOrdersListFragment extends BaseFragment {
    private static final String TAG = "MyOrdersFragment";

    private FragmentMyOrdersListBinding binding;

    private UserViewModel userViewModel;
    private OrderCancelViewModel orderCancelViewModel;
    private OrderViewModel orderViewModel;
    private OrderAdapter orderAdapter;
    private int containerID, cId;

    public MyOrdersListFragment() {
    }

    public static MyOrdersListFragment newInstance(int containerID) {

        MyOrdersListFragment fragment = new MyOrdersListFragment();
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
            cId = containerID;
        }

        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        orderCancelViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderCancelViewModel.class);
        orderViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderViewModel.class);
        orderViewModel.getOrderList(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, userViewModel.getSignInResponse().getValue().getCustomerId(), new NetworkOperations(true));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.order_history));
        binding = FragmentMyOrdersListBinding.inflate(inflater, container, false);

        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderViewModel.getOrderListResponse().observe(this, orderListResponse -> {

            if (orderListResponse != null) {

                if (orderListResponse.getOrders()==null) {

                    binding.linearLayoutContent.setVisibility(View.GONE);
                    binding.emptyView.linearEmpty.setVisibility(View.VISIBLE);
                    binding.emptyView.txtEmpty.setText(orderListResponse.getSuccess_msg());
                    binding.emptyView.imgEmpty.setImageResource(R.drawable.placed_order_empty);
                } else {

                    binding.linearLayoutContent.setVisibility(View.VISIBLE);
                    binding.emptyView.linearEmpty.setVisibility(View.GONE);

                    orderAdapter = new OrderAdapter(
                            getActivity(), orderListResponse.getOrders().getOrder(), onItemClick, onTrackClick, onCancelClick,onReorderClick,
                            Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                    );
                    binding.recyclerViewOrders.setAdapter(orderAdapter);

                }


            }

        });

        return binding.getRoot();
    }

    OrderAdapter.OnItemClick onItemClick = (position, orderViewResult) -> {
        initiateFoodItemFragment(orderViewResult.getOrderIdentifyno());
    };

    OrderAdapter.OnTrackClick onTrackClick = (position, orderViewResult) -> {
        initiateOrderTrackFragment(orderViewResult);
    };

    OrderAdapter.OnReorderClick onReorderClick = (position, orderViewResult) -> {


        showMessage("Alert", "Are you sure you want to reorder?", "YES", "NO", dialogInterface -> {
            dialogInterface.dismiss();
            initiateReorderTrackFragment(orderViewResult);
        }, dialogInterface -> {
            dialogInterface.dismiss();
        });


    };

    private void initiateReorderTrackFragment(Order orderViewResult) {
        startActivity(new Intent(getActivity(), CartActivity.class).putExtra("ISFROMREORDER",true).putExtra("OrderNumber",orderViewResult.getOrderIdentifyno()));
    getActivity().finish();
    }

    OrderAdapter.OnCancelClick onCancelClick = (position, orderViewResult) -> {


        showMessage("Alert", "Are you sure you want to delete?", "YES", "NO", dialogInterface -> {
            dialogInterface.dismiss();
            cancelOrder(orderViewResult);

        }, dialogInterface -> {
            dialogInterface.dismiss();
        });


    };

    private void cancelOrder(Order order) {

        orderCancelViewModel.cancelOrder(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, order.getOrderIdentifyno(), new NetworkOperations(true));
        orderCancelViewModel.getOrderCancelResponse().observe(this, orderCancelResponse -> {

            if (orderCancelResponse != null) {
                if (orderCancelResponse.getError().equals(0L)) {
                    order.setOrderStatusMsg("Cancelled");
                    orderAdapter.notifyDataSetChanged();
                }

                showMessage("Alert", orderCancelResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                    dialogInterface.dismiss();
                }, null);

                orderCancelViewModel.getOrderCancelResponse().removeObservers(this);
            }
        });

    }

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

    private void initiateFoodItemFragment(String orderID) {
//        MyOrderDetailFragment myOrderDetailFragment = MyOrderDetailFragment.newInstance(containerID, orderID);
//
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(containerID, myOrderDetailFragment);
//        transaction.addToBackStack(MyOrderDetailFragment.getTAG());
//        transaction.commit();
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("CONTAINER_ID", cId);
        intent.putExtra("ORDER_ID", orderID);
        getActivity().startActivity(intent);
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

    private void initiateOrderTrackFragment(Order orderViewResult) {
        OrderTrackFragment orderTrackFragment = OrderTrackFragment.newInstance(containerID, orderViewResult.getOrderIdentifyno());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, orderTrackFragment);
        transaction.addToBackStack(OrderTrackFragment.getTAG());
        transaction.commit();
    }

}
