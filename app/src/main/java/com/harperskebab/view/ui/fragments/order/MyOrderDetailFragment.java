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

import com.harperskebab.databinding.FragmentMyOrderDetailBinding;
import com.harperskebab.model.OrderDetailItem;
import com.harperskebab.model.OrderFoodItem;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.OrderItemAdapter;
import com.harperskebab.view.ui.activities.TrackingActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.OrderDetailViewModel;
import com.harperskebab.viewmodel.OrderViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.text.DecimalFormat;
import java.util.List;

public class MyOrderDetailFragment extends BaseFragment {
    private static final String TAG = "MyOrderDetailFragment";

    private FragmentMyOrderDetailBinding binding;

    private OrderViewModel orderViewModel;
    private OrderDetailViewModel orderDetailViewModel;
    private int containerID;
    private String orderID;

    public MyOrderDetailFragment() {
    }

    public static MyOrderDetailFragment newInstance(int containerID, String orderID) {

        MyOrderDetailFragment fragment = new MyOrderDetailFragment();
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
        orderViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderViewModel.class);
        orderDetailViewModel = ViewModelFactory.getInstance(getActivity()).create(OrderDetailViewModel.class);
        orderDetailViewModel.getOrderDetail(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, orderID, new NetworkOperations(true));

        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        orderDetailViewModel.getOrderDetailResponse().observe(this, orderDetailResponse -> {
            //todo

            if (orderDetailResponse != null) {
                binding.constraintOrderDetails.setVisibility(View.VISIBLE);
                OrderDetailItem orderDetailItem = orderDetailResponse.getOrderDetailItem().get(0);

                binding.textViewOrderNumber.setText(String.format(languageViewModel.getLanguageResponse().getValue().getOrderNumber() + " %s", orderDetailResponse.getOrderDetailItem().get(0).getOrderNumber()));
                binding.textViewOrderStatus.setText(orderDetailItem.getOrderStatusMsg());
                double subTot = 0, dilvChrg = 0;
                if (!orderDetailItem.getSubTotal().equals("")) {
                    binding.txtSubTotal.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + orderDetailItem.getSubTotal());
                    subTot = Double.parseDouble(orderDetailItem.getSubTotal());
                } else {
                    binding.txtSubTotal.setVisibility(View.GONE);
                }
                if (!orderDetailItem.getDeliveryCharge().equals("")) {

                    if(orderDetailItem.getDeliveryCharge().equalsIgnoreCase("0.00")){
                        binding.relativeLayoutToPay.setVisibility(View.GONE);
                        binding.textViewPay.setVisibility(View.GONE);
                        binding.txtDeliveryCharge.setVisibility(View.GONE);
                    }
                    else {
                        binding.relativeLayoutToPay.setVisibility(View.VISIBLE);
                        binding.txtDeliveryCharge.setVisibility(View.VISIBLE);
                        binding.textViewPay.setVisibility(View.VISIBLE);
                        binding.txtDeliveryCharge.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + orderDetailItem.getDeliveryCharge());
                        dilvChrg = Double.parseDouble(orderDetailItem.getDeliveryCharge());
                    }
                } else {
                    binding.relativeLayoutToPay.setVisibility(View.GONE);
                    binding.txtDeliveryCharge.setVisibility(View.GONE);
                    binding.textViewPay.setVisibility(View.GONE);
                }
                binding.txtOrderType.setText(orderDetailItem.getOrderType());
                double total = subTot + dilvChrg;
                DecimalFormat REAL_FORMATTER = new DecimalFormat("0.##");

                binding.txtTotalPrice.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + REAL_FORMATTER.format(total));
                List<OrderFoodItem> foods = orderDetailResponse.getOrderDetailItem().get(0).getOrderFoodItem();

                Double totalAmount = 0.00D;
                for (OrderFoodItem food : foods) {
                    totalAmount = totalAmount + (Double.parseDouble(food.getMenuprice()) * food.getQuantity());
                }


                Double totalDiscount = Double.parseDouble(orderDetailItem.getDiscountPrice()) + Double.parseDouble(orderDetailItem.getCouponPrice());

                setValueInTextView(binding.billDetail.relativeLayout1, binding.billDetail.textViewItem1, languageViewModel.getLanguageResponse().getValue().getFoodItem(), binding.billDetail.textViewItem1Price, totalAmount, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout2, binding.billDetail.textViewItem2, languageViewModel.getLanguageResponse().getValue().getFoodDiscount(), binding.billDetail.textViewItem2Price, 0L, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout3, binding.billDetail.textViewItem3, languageViewModel.getLanguageResponse().getValue().getCouponDiscount(), binding.billDetail.textViewItem3Price, Double.parseDouble(orderDetailItem.getCouponPrice()), orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout4, binding.billDetail.textViewItem4, languageViewModel.getLanguageResponse().getValue().getLoyaltyDiscount(), binding.billDetail.textViewItem4Price, Double.parseDouble(orderDetailItem.getPayByLoyality()), orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout5, binding.billDetail.textViewItem5, languageViewModel.getLanguageResponse().getValue().getRestaurantDiscount(), binding.billDetail.textViewItem5Price, Double.parseDouble(orderDetailItem.getDiscountPrice()), orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout6, binding.billDetail.textViewItem6, languageViewModel.getLanguageResponse().getValue().getTotalDiscount(), binding.billDetail.textViewItem6Price, 0L, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout7, binding.billDetail.textViewItem7, languageViewModel.getLanguageResponse().getValue().getSubtotal(), binding.billDetail.textViewItem7Price, Double.parseDouble(orderDetailItem.getSubTotal()), orderDetailItem.getCurrency());

                double deliveryCharge;
                if (orderDetailItem.getOrderType().equalsIgnoreCase("Delivery")) {
                    deliveryCharge = Double.parseDouble(orderDetailItem.getDeliveryCharge());
                } else {
                    deliveryCharge = 0.0D;
                }

                setValueInTextView(binding.billDetail.relativeLayout8, binding.billDetail.textViewItem8, languageViewModel.getLanguageResponse().getValue().getDeliveryCharge(), binding.billDetail.textViewItem8Price, deliveryCharge, orderDetailItem.getCurrency());

                double serviceFees = Double.parseDouble(orderDetailItem.getServiceFees());
                setValueInTextView(binding.billDetail.relativeLayout9, binding.billDetail.textViewItem9, languageViewModel.getLanguageResponse().getValue().getServiceCharge(), binding.billDetail.textViewItem9Price, serviceFees, orderDetailItem.getCurrency());

                double packageFees = Double.parseDouble(orderDetailItem.getPackageFees());
                setValueInTextView(binding.billDetail.relativeLayout10, binding.billDetail.textViewItem10, languageViewModel.getLanguageResponse().getValue().getPackageFees(), binding.billDetail.textViewItem10Price, packageFees, orderDetailItem.getCurrency());

                double salesTax = Double.parseDouble(orderDetailItem.getSalesTaxAmount());
                setValueInTextView(binding.billDetail.relativeLayout11, binding.billDetail.textViewItem11, languageViewModel.getLanguageResponse().getValue().getSaleTax(), binding.billDetail.textViewItem11Price, salesTax, orderDetailItem.getCurrency());

                double vatTax = Double.parseDouble(orderDetailItem.getVatTax());
                setValueInTextView(binding.billDetail.relativeLayout12, binding.billDetail.textViewItem12, languageViewModel.getLanguageResponse().getValue().getVatTax(), binding.billDetail.textViewItem12Price, vatTax, orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout13, binding.billDetail.textViewItem13, languageViewModel.getLanguageResponse().getValue().getFoodTax(), binding.billDetail.textViewItem13Price, Double.parseDouble(orderDetailItem.getGetFoodTaxTotal7()), orderDetailItem.getCurrency());

                setValueInTextView(binding.billDetail.relativeLayout14, binding.billDetail.textViewItem14, languageViewModel.getLanguageResponse().getValue().getDrinkTax(), binding.billDetail.textViewItem14Price, Double.parseDouble(orderDetailItem.getGetFoodTaxTotal19()), orderDetailItem.getCurrency());

                double riderTipAmount = Double.parseDouble(orderDetailItem.getExtraTipAddAmount());

                setValueInTextView(binding.billDetail.relativeLayout15, binding.billDetail.textViewItem15, languageViewModel.getLanguageResponse().getValue().getRiderTip(), binding.billDetail.textViewItem15Price, riderTipAmount, orderDetailItem.getCurrency());

//                setValueInTextView(binding.billDetail.relativeLayoutToPay, binding.billDetail.textViewPay, languageViewModel.getLanguageResponse().getValue().getToPay(), binding.billDetail.textViewTotalPrice, Double.parseDouble(orderDetailItem.getOrderPrice()), orderDetailItem.getCurrency());
                if (orderDetailItem.getOrderFoodItem().size() != 0) {
                    OrderItemAdapter orderItemAdapter = new OrderItemAdapter(getActivity(), orderDetailItem.getOrderFoodItem());
                    binding.recyclerViewOrderItems.setAdapter(orderItemAdapter);
                }

                if (orderDetailItem.getOrderStatusMsg().equalsIgnoreCase("Cancelled")) {

                    binding.buttonOrderTrack.setVisibility(View.GONE);
                    binding.buttonOrderReview.setVisibility(View.GONE);

                } else if (orderDetailItem.getOrderStatusMsg().equalsIgnoreCase("Delivered")) {

                    binding.buttonOrderTrack.setVisibility(View.GONE);
                    if (orderDetailItem.getReviewDone().equalsIgnoreCase("no")) {
                        binding.buttonOrderReview.setVisibility(View.VISIBLE);
                    } else {
                        binding.buttonOrderReview.setVisibility(View.GONE);
                    }

                } else {

                    binding.buttonOrderTrack.setVisibility(View.VISIBLE);
                    binding.buttonOrderReview.setVisibility(View.GONE);

                }

                orderDetailViewModel.getOrderDetailResponse().removeObservers(this);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getOrderDetails());
        binding = FragmentMyOrderDetailBinding.inflate(inflater, container, false);

        binding.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(getActivity()));


        binding.buttonOrderTrack.setOnClickListener(this::onClick);
        binding.buttonOrderReview.setOnClickListener(this::onClick);

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
        if (v.getId() == binding.buttonOrderTrack.getId()) {
            initiateOrderTrackFragment();
        } else if (v.getId() == binding.buttonOrderReview.getId()) {
            initiateOrderReviewFragment();
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

    private void initiateOrderTrackFragment() {
//        OrderTrackFragment orderTrackFragment = OrderTrackFragment.newInstance(containerID, orderID);
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(containerID, orderTrackFragment);
//        transaction.addToBackStack(OrderTrackFragment.getTAG());
//        transaction.commit();
        Intent intent = new Intent(getActivity(), TrackingActivity.class);
        intent.putExtra("CONTAINER_ID", containerID);
        intent.putExtra("ORDER_ID", orderID);
        getActivity().startActivity(intent);
    }

    private void initiateOrderReviewFragment() {
        OrderReviewFragment orderReviewFragment = OrderReviewFragment.newInstance(containerID, orderID);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, orderReviewFragment);
        transaction.addToBackStack(OrderReviewFragment.getTAG());
        transaction.commit();
    }


}
