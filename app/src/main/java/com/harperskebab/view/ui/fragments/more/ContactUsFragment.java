package com.harperskebab.view.ui.fragments.more;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.FragmentContactUsBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Validation;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.ContactUsViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class ContactUsFragment extends BaseFragment {
    private static final String TAG = "ContactUsFragment";

    private FragmentContactUsBinding binding;
    private ContactUsViewModel contactUsViewModel;
    private UserViewModel userViewModel;

    private int containerID;

    public ContactUsFragment() {
    }

    public static ContactUsFragment newInstance(int containerID) {

        ContactUsFragment fragment = new ContactUsFragment();
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
        contactUsViewModel = ViewModelFactory.getInstance(getActivity()).create(ContactUsViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getContactUs());
        binding = FragmentContactUsBinding.inflate(inflater, container, false);
        userViewModel = ViewModelFactory.getInstance(getContext()).create(UserViewModel.class);
        binding.textViewRestaurantName.setText(restaurantViewModel.getRestaurant().getValue().getRestaurantName());
        binding.textViewAddress.setText(restaurantViewModel.getRestaurant().getValue().getRestaurantAddress());
        binding.textViewEmail.setText(restaurantViewModel.getRestaurant().getValue().getRestaurantContactEmail());
        binding.textViewCall.setText(restaurantViewModel.getRestaurant().getValue().getRestaurantContactMobile());
        binding.buttonSubmit.setOnClickListener(this::onClick);
        restaurantViewModel.getRestaurant().observe(this, restaurant ->{
        });
        userViewModel.getSignInResponse().observeForever(signInResponse -> {
           if(signInResponse.getSuccess()==0) {
               binding.editTextName.setText(signInResponse.getUserName());
               binding.editTextMobile.setText(signInResponse.getUserPhone());
               binding.editTextEmail.setText(signInResponse.getUserEmail());
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

        if (v.getId() == binding.buttonSubmit.getId()) {

            String name = binding.editTextName.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String mobile = binding.editTextMobile.getText().toString();
            String message = binding.editTextMessage.getText().toString();

            if (name.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterYourName());
            } else if (email.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterYourEmail());
            } else if (Validation.isValidEmail(email)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterYourEmail());
            } else if (mobile.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterYourMobile());
            } else if (mobile.length() < 10) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterYourMobile());
            } else if (message.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterMessage());
            } else {

                contactUsViewModel.submitContactUs(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, name, email, mobile, message, new NetworkOperations(true));
                contactUsViewModel.getContactUsResponse().observe(this, contactUsResponse -> {

                    if (contactUsResponse != null) {
                        if (contactUsResponse.getError().equals(1L)) {

                            showMessage("Alert", contactUsResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                                goBack();
                                dialogInterface.dismiss();
                            }, dialogInterface -> {
                                dialogInterface.dismiss();
                            });

                        } else {
                            showMessage("Alert", contactUsResponse.getError_msg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);

                        }

                        contactUsViewModel.getContactUsResponse().removeObservers(this);
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