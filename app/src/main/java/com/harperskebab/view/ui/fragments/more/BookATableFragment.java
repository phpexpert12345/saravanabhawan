package com.harperskebab.view.ui.fragments.more;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogNoOfPersonBinding;
import com.harperskebab.databinding.FragmentBookATableBinding;
import com.harperskebab.model.Table;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.TableAdapter;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.EatInViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.Calendar;

public class BookATableFragment extends BaseFragment {
    private static final String TAG = "BookATableFragment";

    private FragmentBookATableBinding binding;
    private EatInViewModel eatInViewModel;
    private Table selectedTable = null;
    private int noOfPerson = 0;
    private UserViewModel userViewModel;
    private int containerID;
    String BranchId;

    public BookATableFragment() {
    }

    public static BookATableFragment newInstance(int containerID) {

        BookATableFragment fragment = new BookATableFragment();
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
        BranchId=  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId","2");
        userViewModel = ViewModelFactory.getInstance(getActivity()).create(UserViewModel.class);
        eatInViewModel = ViewModelFactory.getInstance(getActivity()).create(EatInViewModel.class);
        eatInViewModel.getTableList(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true),BranchId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getBookATable());
        binding = FragmentBookATableBinding.inflate(inflater, container, false);
        if (userViewModel.getSignInResponse().getValue()!=null) {
            binding.editTextCustomerName.setText(userViewModel.getSignInResponse().getValue().getFirstName() + " " + userViewModel.getSignInResponse().getValue().getLastName());
            binding.editTextMobileNumber.setText(userViewModel.getSignInResponse().getValue().getUserPhone());
        }
        String branchName  = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("BRANCH_NAME", "");
        String branchAddress  = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("BRANCH_ADDRESS", "");
        binding.txtName.setText(branchName);
        binding.txtAddress.setText(branchAddress);
        binding.editTextBookingDate.setInputType(InputType.TYPE_NULL);
        binding.editTextBookingDate.setOnClickListener(this::onEditTextBookingDateClick);
        binding.editTextBookingDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) onEditTextBookingDateClick(v);
        });

        binding.editTextBookingTime.setInputType(InputType.TYPE_NULL);
        binding.editTextBookingTime.setOnClickListener(this::onEditTextBookingTimeClick);
        binding.editTextBookingTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) onEditTextBookingTimeClick(v);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eatInViewModel.getTableList().observe(this, tables -> {
            if (tables != null) {
binding.nestedScrollTable.setVisibility(View.VISIBLE);
                TableAdapter tableAdapter = new TableAdapter(getActivity(), tables, -1L, (position, table) -> {
                    showDialogForNoOfPersonConfirmation(table);
                });

                binding.recyclerView.setAdapter(tableAdapter);

                eatInViewModel.getTableList().removeObservers(this);
            }
        });

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
            String customerName = binding.editTextCustomerName.getText().toString();
            String customerMobile = binding.editTextMobileNumber.getText().toString();

            String bookingDate = binding.editTextBookingDate.getText().toString();
            String bookingTime = binding.editTextBookingTime.getText().toString();

            String specialInstructions = binding.editTextSpecialInstructions.getText().toString();

            if (customerName.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterCustomerName());
            } else if (customerMobile.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterCustomerMobile());
            } else if (bookingDate.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterBootingDate());
            } else if (selectedTable == null) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPLEASECHOOSEONEOPTION());
            } else if (bookingTime.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterBookingTime());
            } else {
                initiateBookATableConfirmationFragment(customerName, customerMobile, bookingDate, bookingTime, specialInstructions);
            }

        }

    }

    public void onEditTextBookingDateClick(View view) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (datePicker, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, monthOfYear);
            calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String selectedDateString = Utility.DATE_FORMAT.format(calendar1.getTime());
            binding.editTextBookingDate.setText(selectedDateString);
        };

        new DatePickerDialog(getActivity(), date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void onEditTextBookingTimeClick(View view) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener time = (timePicker, hourOfDay, minute) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar1.set(Calendar.MINUTE, minute);

            String selectedTimeString = Utility.TIME_FORMAT.format(calendar1.getTime());
            binding.editTextBookingTime.setText(selectedTimeString);
        };

        new TimePickerDialog(getActivity(), time,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false)
                .show();
    }
    int noPerson;
    private void showDialogForNoOfPersonConfirmation(Table table) {
        DialogNoOfPersonBinding dialogNoOfPersonBinding = DialogNoOfPersonBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogNoOfPerson = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogNoOfPerson.setContentView(dialogNoOfPersonBinding.getRoot());

        dialogNoOfPersonBinding.edtNoOfPerson.setText(table.getNumberOfPeople());
        noPerson=Integer.parseInt(table.getNumberOfPeople());
        dialogNoOfPersonBinding.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noPerson<Integer.parseInt(table.getNumberOfPeople())){
                    noPerson++;
                    dialogNoOfPersonBinding.edtNoOfPerson.setText(String.valueOf(noPerson));
                }
            }
        });
        dialogNoOfPersonBinding.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noPerson>1){
                    noPerson--;
                    dialogNoOfPersonBinding.edtNoOfPerson.setText(String.valueOf(noPerson));
                }
            }
        });
        dialogNoOfPersonBinding.buttonSubmit.setOnClickListener(v -> {
             noOfPerson = Integer.parseInt(dialogNoOfPersonBinding.edtNoOfPerson.getText().toString());
            if (Integer.parseInt(table.getNumberOfPeople()) < noOfPerson) {
                PopMessage.makeLongToast(getActivity(), getString(R.string.selected_table_has_less_capacity));
            } else {
                this.selectedTable = table;
                this.noOfPerson = noOfPerson;
                dialogNoOfPerson.dismiss();
            }
        });

        dialogNoOfPerson.setTitle(getString(R.string.confirmation));
        dialogNoOfPerson.show();

    }

    private void initiateBookATableConfirmationFragment(String customerName, String customerMobile, String bookingDate, String bookingTime, String specialInstructions) {
        BookATableConfirmationFragment bookATableConfirmationFragment = BookATableConfirmationFragment.newInstance(containerID, customerName, customerMobile, bookingDate, bookingTime, specialInstructions, selectedTable, noOfPerson);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, bookATableConfirmationFragment);
        transaction.addToBackStack(BookATableConfirmationFragment.getTAG());
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