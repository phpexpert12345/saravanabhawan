package com.harperskebab.view.ui.fragments.auth.signin;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentForgetPasswordBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Validation;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.ForgetPasswordViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class ForgetPasswordFragment extends BaseFragment {
    private static final String TAG = "ForgetPasswordFragment";

    private FragmentForgetPasswordBinding binding;
    private ForgetPasswordViewModel forgetPasswordViewModel;

    private int containerID;

    public ForgetPasswordFragment() {

    }

    public static ForgetPasswordFragment newInstance(int containerID) {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
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
        forgetPasswordViewModel = ViewModelFactory.getInstance(getActivity()).create(ForgetPasswordViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.forget_password));
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);

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
            String emailID = binding.editTextEmail.getText().toString();
            if (emailID.equals("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else {

                forgetPasswordViewModel.forgetPassword(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, emailID, new NetworkOperations(true));
                forgetPasswordViewModel.getForgetPasswordResponse().observe(this, forgetPasswordResponse -> {

                    if (forgetPasswordResponse != null) {

                        showMessage("Alert", forgetPasswordResponse.getErrorMsg(), "OK", null, dialogInterface -> {

                            if (forgetPasswordResponse.getError().equals(0L)) {
                                getActivity().finish();
                            }
                            dialogInterface.dismiss();
                        }, null);


                        forgetPasswordViewModel.getForgetPasswordResponse().removeObservers(this);
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

}
