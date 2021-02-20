package com.harperskebab.view.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogNewAddressBinding;
import com.harperskebab.databinding.FragmentAddressBinding;
import com.harperskebab.model.Deliveryaddress;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.SharedPrefrenceObj;
import com.harperskebab.view.adapter.AddressAdapter;
import com.harperskebab.view.ui.activities.AddressFromPostCodeActivity;
import com.harperskebab.viewmodel.AddressViewModel;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressFragment extends BaseFragment {
    private static final String TAG = "AddressFragment";
    String address,postCode;
    private FragmentAddressBinding binding;

    private UserViewModel userViewModel;
    private CartViewModel cartViewModel;
    private AddressViewModel addressViewModel;
    DialogNewAddressBinding dialogNewAddressBinding;
    private AddressAdapter addressAdapter;

    private int containerID;

    public AddressFragment() {
    }

    public static AddressFragment newInstance(int containerID) {

        AddressFragment fragment = new AddressFragment();
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
        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        addressViewModel = ViewModelFactory.getInstance(getActivity()).create(AddressViewModel.class);
        downloadAddress();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getAddress());
        binding = FragmentAddressBinding.inflate(inflater, container, false);

        binding.recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
         postCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("POST_CODE", "");
         address = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("ADDRESS", "");

        addressViewModel.getGetAddressResponse().observe(this, addressResponse -> {
            //TODO
            if (addressResponse != null) {
                if (addressResponse.getAddress().getDeliveryaddress().size() == 0) {
                    showNewAddressDialog();

                    binding.constraintLayoutContent.setVisibility(View.GONE);
                    binding.emptyView.getRoot().setVisibility(View.VISIBLE);
                    binding.emptyView.textViewErrorMessage.setText(addressResponse.getSuccessMsg());
                } else {
                    binding.constraintLayoutContent.setVisibility(View.VISIBLE);
                    binding.emptyView.getRoot().setVisibility(View.GONE);
                }
                addressAdapter = new AddressAdapter(getActivity(), addressResponse.getAddress().getDeliveryaddress(), onDeleteClick);
                binding.recyclerViewAddress.setAdapter(addressAdapter);
            }

        });

        if (cartViewModel.getToPay().getValue() == null) {
            binding.buttonConfirmDeliveryAddress.setVisibility(View.GONE);
        }

        binding.floatingActionButtonAddAddress.setOnClickListener(this::onClick);
        binding.buttonConfirmDeliveryAddress.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    private AddressAdapter.OnDeleteClick onDeleteClick = (position, deliveryAddress) -> {
        //todo
        showMessage("Alert", "Are you sure you want to delete?", "YES", "NO", dialogInterface -> {
            deleteAddress(deliveryAddress);
            dialogInterface.dismiss();
        }, dialogInterface -> {
            dialogInterface.dismiss();
        });

    };

    private void deleteAddress(Deliveryaddress deliveryAddress) {
        addressViewModel.deleteAddress(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "" + deliveryAddress.getId(), new NetworkOperations(true));

        addressViewModel.getDeleteAddressResponse().observe(this, deleteAddressResponse -> {

            if (deleteAddressResponse != null) {

                if (deleteAddressResponse.getSuccess().equals(1L)) {

                    addressAdapter.remove(deliveryAddress);
                    if (addressAdapter.getItemCount() == 0) {
                        binding.constraintLayoutContent.setVisibility(View.GONE);
                        binding.emptyView.getRoot().setVisibility(View.VISIBLE);
                    }
                    showMessage("Alert", deleteAddressResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                        downloadAddress();
                        dialogInterface.dismiss();
                    }, dialogInterface -> {
                        downloadAddress();
                        dialogInterface.dismiss();
                    });

                } else {
                    showMessage("Alert", deleteAddressResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                        dialogInterface.dismiss();
                    }, null);
                }

                addressViewModel.getDeleteAddressResponse().removeObservers(this);
            }

        });
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

        if (v.getId() == binding.floatingActionButtonAddAddress.getId()) {
            showNewAddressDialog();
        } else if (v.getId() == binding.buttonConfirmDeliveryAddress.getId()) {
            cartViewModel.setSelectedDeliveryAddress(addressAdapter.getselecteddDeliveryaddresses());
            initiatePaymentFragment();
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

    private void initiatePaymentFragment() {
        PaymentFragment paymentFragment = PaymentFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, paymentFragment);
        transaction.addToBackStack(paymentFragment.getTag());
        transaction.commit();
    }


    private void downloadAddress() {
        addressViewModel.getAddress(getActivity(), userViewModel.getSignInResponse().getValue().getCustomerId(), "", "", new NetworkOperations(true));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            if (data != null) {
                String message = data.getStringExtra("ADDRESS");
                String postCode = data.getStringExtra("POSTCODE");
                String response=data.getStringExtra("ADDRESS_FULL");
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("POST_CODE",postCode).putString("ADDRESS",response).apply();
                postCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("POST_CODE", "");
                address = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("ADDRESS", "");
                dialogNewAddressBinding.editTextPostCode.setText(postCode);
                if (!address.equalsIgnoreCase("")) {
                    try {
                        JSONObject obj=new JSONObject(address);
                        String city=obj.getString("town_or_city");
                        String houseNo=obj.getString("line_2");
                        dialogNewAddressBinding.editTextHouse.setText(houseNo);
//                dialogNewAddressBinding.editTextStreetName.setText(strONE);
                        dialogNewAddressBinding.editTextCity.setText(city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//        if (separated.length > 4) {
//            String strFive=separated[4];
//            dialogNewAddressBinding.editTextCity.setText(strFive);
//        }else {

                }


            }
        }
    }

    private void showNewAddressDialog() {
        dialogNewAddressBinding = DialogNewAddressBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogNewAddress = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogNewAddress.setContentView(dialogNewAddressBinding.getRoot());
        postCode = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("POST_CODE", "");
        address = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("ADDRESS", "");
        dialogNewAddressBinding.editTextPostCode.setText(postCode);
        if (!address.equalsIgnoreCase("")) {
            try {
                JSONObject response=new JSONObject(address);
                String city=response.getString("town_or_city");
                String houseNo=response.getString("line_2");
                dialogNewAddressBinding.editTextHouse.setText(houseNo);
//                dialogNewAddressBinding.editTextStreetName.setText(strONE);
                dialogNewAddressBinding.editTextCity.setText(city);
            } catch (JSONException e) {
                e.printStackTrace();
            }


//        if (separated.length > 4) {
//            String strFive=separated[4];
//            dialogNewAddressBinding.editTextCity.setText(strFive);
//        }else {

        }
//        }



        dialogNewAddressBinding.imageViewClose.setOnClickListener(v -> dialogNewAddress.dismiss());
        dialogNewAddressBinding.editTextPostCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddressFromPostCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        dialogNewAddressBinding.buttonSubmit.setOnClickListener(v -> {

            String addressTitle = dialogNewAddressBinding.editTextAddressTitle.getText().toString().replace("'", "");

            String postCode = dialogNewAddressBinding.editTextPostCode.getText().toString().replace("'", "");
            String house = dialogNewAddressBinding.editTextHouse.getText().toString().replace("'", "");
            String flatName = dialogNewAddressBinding.editTextFlatName.getText().toString().replace("'", "");
            String streetName = dialogNewAddressBinding.editTextStreetName.getText().toString().replace("'", "");
            String city = dialogNewAddressBinding.editTextCity.getText().toString().replace("'", "");
            String phone = dialogNewAddressBinding.editTextPhoneNo.getText().toString().replace("'", "");


            if (addressTitle.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterAddressTitle());
            }else if (postCode.equals("")) {
                    PopMessage.makeLongToast(getActivity(), "Enter Postcode");
            } else if (house.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterHouseDoorNumber());
            }  else if (streetName.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterStreetName());
             } else if (city.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterCity());
            } else if (phone.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else {

                addressViewModel.addAddress(getActivity(), userViewModel.getSignInResponse().getValue().getCustomerId(), addressTitle, house, flatName, streetName,
                        city, "", postCode, "", "", phone, Constant.API.LANGUAGE_CODE, Constant.API.FOOD_KEY, new NetworkOperations(true));

                addressViewModel.getAddAddressResponse().observe(this, addAddressResponse -> {
                    if (addAddressResponse.getSuccess().equals(1L)) {

                        dialogNewAddress.dismiss();
                        addressViewModel.getAddAddressResponse().removeObservers(this);

                        showMessage("Alert", addAddressResponse.getSuccessMsg(), "OK", "CLOSE", dialogInterface -> {
                            downloadAddress();
                            dialogInterface.dismiss();
                        }, dialogInterface -> {
                            dialogInterface.dismiss();
                        });

                    } else {

                        showMessage("Alert", addAddressResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                            dialogInterface.dismiss();
                        }, null);

                    }
                });
            }
        });

        dialogNewAddress.show();
    }
}
