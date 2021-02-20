package com.harperskebab.view.ui.fragments.more;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogAuthBinding;
import com.harperskebab.databinding.DialogPayBinding;
import com.harperskebab.databinding.FragmentBookATableConfirmationBinding;
import com.harperskebab.model.Table;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.RFInterface;
import com.harperskebab.network.retrofit.responsemodels.RmSignInResponse;
import com.harperskebab.network.retrofit.responsemodels.RmStripeUpdateChargeResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.utils.Validation;
import com.harperskebab.view.ui.activities.BookATableCompleteActivity;
import com.harperskebab.view.ui.activities.ForgetPasswordActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.BookATableViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookATableConfirmationFragment extends BaseFragment {
    private static final String TAG = "BookATableConfirmationF";

    private FragmentBookATableConfirmationBinding binding;
    private UserViewModel userViewModel;
    private BookATableViewModel bookATableViewModel;

    private int containerID;
    private String customerName;
    private String customerMobile;
    private String bookingDate;
    private String bookingTime;
    private String specialInstructions;
    private Table table;
    private int noOfPerson = 0;

    private Double totalBillAmount;
    private Double discountAmount, discountPercentage, subtotalDiscount;
    private Double serviceChargeAmount, serviceChargePercentage;
    private Double finalBillAmount;
    private Double depositAmountForBooking;


    private RFInterface rfInterface = Utility.getRetrofitInterface();
    private ProgressDialog progressDialog;

    public BookATableConfirmationFragment() {
    }

    public static BookATableConfirmationFragment newInstance(
            int containerID, String customerName, String customerMobile, String bookingDate, String bookingTime, String specialInstructions, Table table, int noOfPerson
    ) {

        BookATableConfirmationFragment fragment = new BookATableConfirmationFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString(Constant.Data.CUSTOMER_NAME, customerName);
        args.putString(Constant.Data.CUSTOMER_MOBILE, customerMobile);
        args.putString(Constant.Data.BOOKING_DATE, bookingDate);
        args.putString(Constant.Data.BOOKING_TIME, bookingTime);
        args.putString(Constant.Data.SPECIAL_INSTRUCTIONS, specialInstructions);
        args.putSerializable(Constant.Data.TABLE, table);
        args.putInt(Constant.Data.NO_OF_PERSON, noOfPerson);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            customerName = getArguments().getString(Constant.Data.CUSTOMER_NAME);
            customerMobile = getArguments().getString(Constant.Data.CUSTOMER_MOBILE);
            bookingDate = getArguments().getString(Constant.Data.BOOKING_DATE);
            bookingTime = getArguments().getString(Constant.Data.BOOKING_TIME);
            specialInstructions = getArguments().getString(Constant.Data.SPECIAL_INSTRUCTIONS);
            table = (Table) getArguments().getSerializable(Constant.Data.TABLE);
            noOfPerson = getArguments().getInt(Constant.Data.NO_OF_PERSON);
        }
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        bookATableViewModel = ViewModelFactory.getInstance(getActivity()).create(BookATableViewModel.class);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getConfirmationBooking());
        binding = FragmentBookATableConfirmationBinding.inflate(inflater, container, false);


        if (!restaurantViewModel.getRestaurant().getValue().getRestaurantCardacceptAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewCreditDebitCard.setVisibility(View.GONE);
        }

        if (!restaurantViewModel.getRestaurant().getValue().getPayPaypalAvailable().equalsIgnoreCase("yes")) {
            binding.cardViewPaypal.setVisibility(View.GONE);
        }

        binding.textViewTableNo.setText(table.getTableNumber());
        binding.textViewBookingDate.setText(bookingDate);
        binding.textViewBookingTime.setText(bookingTime);
        binding.textViewOnOfPerson.setText("" + noOfPerson);

        totalBillAmount = new Double(Integer.parseInt(table.getTablePerPersonCharge()) * noOfPerson);

        String currentDay = Utility.DAY_FORMAT.format(new Date());
        String[] discountDays = table.getDiscountAvailableDays().split(",");

        boolean flag = false;

        for (String day : discountDays) {
            if (day.equalsIgnoreCase(currentDay)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            discountPercentage = new Double(table.getTableDiscountAmount());
            discountAmount = (discountPercentage * totalBillAmount) / 100;
        } else {
            discountPercentage = 0.0;
            discountAmount = 0.0;
        }

        serviceChargePercentage = new Double(table.getTableServiceChargeAmount());
        serviceChargeAmount = (serviceChargePercentage * totalBillAmount) / 100;

        subtotalDiscount = totalBillAmount - discountAmount;

        finalBillAmount = subtotalDiscount + serviceChargeAmount;

        depositAmountForBooking = (finalBillAmount * Integer.parseInt(table.getMinimumDepositPercentage())) / 100;

        binding.textViewTotalBillAmount.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(totalBillAmount));

        binding.textViewTotalDiscount.setText(String.format(languageViewModel.getLanguageResponse().getValue().getTotalDiscount() + "(%s%%):", discountPercentage));

        binding.textViewTotalDiscountPrice.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(discountAmount));

        binding.textViewSubTotalAfterDiscount.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(subtotalDiscount));

        binding.textViewServiceCharge.setText(String.format(languageViewModel.getLanguageResponse().getValue().getServiceCharge() + "(%s):", serviceChargePercentage));
        binding.textViewServiceChargePrice.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(serviceChargeAmount));
        binding.textViewFinalBillAmount.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(finalBillAmount));
        binding.textViewDepositAmountForBooking.setText(Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()) + " " + Utility.DECIMAL_FORMAT.format(depositAmountForBooking));


        binding.radioButtonCreditDebitCard.setOnClickListener(this::onClick);
        binding.radioButtonPaypal.setOnClickListener(this::onClick);

        binding.cardViewCreditDebitCard.setOnClickListener(this::onClick);
        binding.cardViewPaypal.setOnClickListener(this::onClick);

        binding.buttonConfirmAndPay.setOnClickListener(this::onClick);

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

        if (v.getId() == binding.cardViewCreditDebitCard.getId() || v.getId() == binding.radioButtonCreditDebitCard.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(true);
            binding.radioButtonPaypal.setChecked(false);

        } else if (v.getId() == binding.cardViewPaypal.getId() || v.getId() == binding.radioButtonPaypal.getId()) {

            binding.radioButtonCreditDebitCard.setChecked(false);
            binding.radioButtonPaypal.setChecked(true);

        } else if (v.getId() == binding.buttonConfirmAndPay.getId()) {

            if (userViewModel.getSignInResponse().getValue() == null) {
                showAuthDialog();
            } else {
                if (binding.radioButtonCreditDebitCard.isChecked() || binding.radioButtonPaypal.isChecked()) {
                    if (binding.radioButtonCreditDebitCard.isChecked()) {
                        showPayDialog();
                    } else {
                        //todo
                    }
                } else {
                    PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseSelectPaymentType());
                }
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

    private void showAuthDialog() {
        DialogAuthBinding dialogAuthBinding = DialogAuthBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogAuth = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogAuth.setContentView(dialogAuthBinding.getRoot());

        dialogAuthBinding.tabLayoutAuth.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        dialogAuthBinding.linearLayoutSignIn.setVisibility(View.VISIBLE);
                        dialogAuthBinding.linearLayoutSignUp.setVisibility(View.GONE);

                        break;
                    case 1:
                        dialogAuthBinding.linearLayoutSignIn.setVisibility(View.GONE);
                        dialogAuthBinding.linearLayoutSignUp.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dialogAuthBinding.tabLayoutAuth.selectTab(dialogAuthBinding.tabLayoutAuth.getTabAt(0));

        dialogAuthBinding.buttonForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
        });

        String regex = "\\d+";

        dialogAuthBinding.buttonSignIn.setOnClickListener(v -> {

            String emailID = dialogAuthBinding.editTextSignInEmail.getText().toString();
            String password = dialogAuthBinding.editTextSignInPassword.getText().toString();

            if (emailID.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (password.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPassword());
            } else {
                signIn(emailID, password, dialogAuth);
            }

        });


        dialogAuthBinding.buttonSignUp.setOnClickListener(v -> {

            String fullName = dialogAuthBinding.editTextSignUptFullName.getText().toString();
            String emailID = dialogAuthBinding.editTextSignUpEmail.getText().toString();
            String phone = dialogAuthBinding.editTextSignUpPhone.getText().toString();
            String password = dialogAuthBinding.editTextSignUpPassword.getText().toString();

            int phoneLength = phone.length();

            if (fullName.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterFullName());
            } else if (!fullName.contains(" ")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterFullName());
            } else if (emailID.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (phone.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (!phone.matches(regex)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (phoneLength < 10) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (password.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterPassword());
            } else {
                String firstName, lastname;
                if (fullName.contains(" ")) {
                    firstName = fullName.split(" ")[0];
                    lastname = fullName.substring(fullName.indexOf(' ') + 1);
                } else {
                    firstName = fullName;
                    lastname = "";
                }

                userViewModel.signUp(getActivity(), firstName, lastname, emailID, password, phone, "", "", Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                        "", "", new NetworkOperations(true));

                userViewModel.getSignUpResponse().observe(this, signUpResponse -> {

                    if (signUpResponse != null) {
                        if (signUpResponse.getError().equals(1L)) {
                            signIn(emailID, password, dialogAuth);
                        } else {
                            userViewModel.getSignUpResponse().setValue(null);
                            showMessage("Alert", signUpResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);
                        }
                        userViewModel.getSignUpResponse().removeObservers(this);
                    }
                });
            }
        });

        dialogAuth.show();
    }

    private void signIn(String emailID, String password, Dialog dialog) {
        userViewModel.signIn(getActivity(), emailID, password, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "", "", new NetworkOperations(true));
        userViewModel.getSignInResponse().observe(this, signInResponse -> {

            if (signInResponse != null) {
                if (signInResponse.getSuccess() == 0) {

                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                        showPayDialog();
                        dialogInterface.dismiss();
                    }, dialogInterface -> {
                        dialogInterface.dismiss();
                    });
                    dialog.dismiss();
                } else {
                    userViewModel.setSignInResponse(null);
                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                        dialogInterface.dismiss();
                    }, null);
                }
                userViewModel.getSignInResponse().removeObservers(this);
            }

        });
    }

    private void showPayDialog() {
        DialogPayBinding dialogPayBinding = DialogPayBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogPay = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogPay.setContentView(dialogPayBinding.getRoot());

        dialogPayBinding.buttonPay.setOnClickListener(v -> {

            Card card = dialogPayBinding.cardInputWidget.getCard();

            if (card != null) {

                progressDialog = Utility.createProgressDialog(getActivity());
                new Stripe(getActivity().getApplicationContext(), PaymentConfiguration.getInstance(getActivity().getApplicationContext()).getPublishableKey())
                        .createToken(card, new ApiResultCallback<Token>() {

                            public void onSuccess(Token token) {

                                rfInterface.updateCharge(
                                        Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                                        Utility.DECIMAL_FORMAT.format(depositAmountForBooking), restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole(),
                                        token.getId(), userViewModel.getSignInResponse().getValue().getUserEmail())
                                        .enqueue(new Callback<RmStripeUpdateChargeResponse>() {
                                            @Override
                                            public void onResponse(Call<RmStripeUpdateChargeResponse> call, Response<RmStripeUpdateChargeResponse> response) {
                                                progressDialog.dismiss();
                                                if (response.isSuccessful()) {

                                                    if (response.body().getResponse().equalsIgnoreCase("success")) {
                                                        callBookATableAPI();
                                                    } else {
                                                        Log.d(TAG, "onFailure: Exception: " + response.body().getMessage());
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RmStripeUpdateChargeResponse> call, Throwable t) {
                                                Log.e(TAG, "onFailure: Throwable: ", t);
                                            }
                                        });


                            }

                            public void onError(Exception error) {
                                Log.d("Stripe", error.getLocalizedMessage());
                            }
                        });
            } else {
                PopMessage.makeLongToast(getActivity(), "Please enter card Details");
            }

        });

        dialogPay.show();

    }

    private void callBookATableAPI() {

        RmSignInResponse user = userViewModel.getSignInResponse().getValue();

        bookATableViewModel.bookATable(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, user.getCustomerId(), table.getTableNumber(), customerMobile, bookingDate,
                bookingTime, specialInstructions, "" + noOfPerson, "" + totalBillAmount, "" + discountAmount, "" + subtotalDiscount,
                "" + serviceChargeAmount, "" + finalBillAmount, "" + depositAmountForBooking, new NetworkOperations(true));

        bookATableViewModel.getBookATableResponse().observe(this, bookATableResponse -> {

            if (bookATableResponse != null) {
                if (bookATableResponse.getSuccess().equals(1L)) {

                    showMessage("Alert", bookATableResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {

                        startActivity(
                                new Intent(getActivity(), BookATableCompleteActivity.class)
                                        .putExtra(Constant.Data.TABLE, table)
                        );

                        dialogInterface.dismiss();
                    }, dialogInterface -> {
                        dialogInterface.dismiss();
                    });

                } else {
                    showMessage("Alert", bookATableResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                        dialogInterface.dismiss();
                    }, null);
                }
                bookATableViewModel.getBookATableResponse().removeObservers(this);
            }

        });

    }


}