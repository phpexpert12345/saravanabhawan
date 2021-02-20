package com.harperskebab.view.ui.fragments.order;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentOrderTackBinding;
import com.harperskebab.model.OrderDetailItem;
import com.harperskebab.model.OrderFoodItem;
import com.harperskebab.model.OrderTrackHistory;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.OrderItemAdapter;
import com.harperskebab.view.adapter.TrackOrderAdapter;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.TrackOrderViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

public class OrderTrackFragment extends BaseFragment {
    private static final String TAG = "OrderTrackFragment";

    private FragmentOrderTackBinding binding;
    private TrackOrderViewModel trackOrderViewModel;

    private int containerID;
    private String orderID;

    public OrderTrackFragment() {
    }

    public static OrderTrackFragment newInstance(int containerID, String orderID) {

        OrderTrackFragment fragment = new OrderTrackFragment();
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
        trackOrderViewModel = ViewModelFactory.getInstance(getActivity()).create(TrackOrderViewModel.class);
        trackOrderViewModel.trackOrder(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderID, new NetworkOperations(true));

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderTackBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.track_order));
        binding.recyclerViewOrderTrack.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(getActivity()));

         // Default: true

        trackOrderViewModel.getTrackOrderResponse().observe(this, trackOrderResponse -> {

            if (trackOrderResponse != null) {

                OrderDetailItem orderDetailItem = trackOrderResponse.getOrderDetailItem().get(0);
//                OrderDetailItem.OrderViewResult orderViewResult = orderDetailItem.getOrders().getOrderViewResult().get(0);

                binding.textViewOrderNumber.setText(
                        orderDetailItem.getOrderNumber()
                );

                binding.textViewOrderDate.setText(trackOrderResponse.getOrderDetailItem().get(0).getOrders().getOrderViewResult().get(0).getOrderDate() + " " + trackOrderResponse.getOrderDetailItem().get(0).getOrders().getOrderViewResult().get(0).getOrderTime());
                binding.txtOrderType.setText(trackOrderResponse.getOrderDetailItem().get(0).getOrders().getOrderViewResult().get(0).getOrderType());
                List<OrderFoodItem> orderFoodItem = trackOrderResponse.getOrderFoodItem();
                StringBuilder foodItems = new StringBuilder();
                double price=0.0;
//                for (OrderFoodItem foodItem : orderFoodItem) {
//                    price+=foodItem.getQuantity()*Double.parseDouble(foodItem.getMenuprice());
//                    if(foodItem.getExtraTopping().equalsIgnoreCase("")){
//
//                    }
//                    foodItems.append(foodItems.toString().equals("") ?  foodItem.getQuantity() + " x "+foodItem.getItemsName()  : "\n" + "\n" +foodItem.getQuantity() + " x "+foodItem.getItemsName() );
//                }
                if (orderFoodItem.size()>0) {
                    OrderItemAdapter orderItemAdapter = new OrderItemAdapter(getActivity(), orderFoodItem);
                    binding.recyclerViewOrderItems.setAdapter(orderItemAdapter);
                }


//                binding.textViewFoodItems.setText(foodItems.toString());
//                binding.textViewFoodPrice.setVisibility(View.VISIBLE);
//                binding.textViewFoodPrice.setText(
//                        Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(price)
//                );

                if (orderDetailItem.getOrderType().equalsIgnoreCase("delivery")) {
                    binding.textViewOrderTypeTitle.setText(languageViewModel.getLanguageResponse().getValue().getDeliveredTo());
//                    binding.textViewOrderTypeAddress.setText(orderDetailItem.getCustomerAddress());

                } else if (orderDetailItem.getOrderType().equalsIgnoreCase("pickup")) {
                    binding.textViewOrderTypeTitle.setText(getString(R.string.Pickup_From));
                    binding.textViewOrderTypeAddress.setText(orderDetailItem.getRestaurantAddress());
                } else {
                    binding.textViewOrderTypeTitle.setText("Eat-In");
                    binding.textViewOrderTypeAddress.setText(orderDetailItem.getRestaurantAddress());
                }
                binding.txtOrderStatus.setTextColor(Color.parseColor(orderDetailItem.getOrderStatusColorCode()));
                binding.txtOrderStatus.setText(orderDetailItem.getOrderStatusMsg());
                TrackOrderAdapter trackOrderAdapter = new TrackOrderAdapter(getActivity(), trackOrderResponse.getOrderTrackHistory());
                binding.recyclerViewOrderTrack.setAdapter(trackOrderAdapter);

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
