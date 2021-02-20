package com.harperskebab.view.ui.fragments.user;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.FragmentChangePasswordBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.ProfileViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class ChangePasswordFragment extends BaseFragment {
    private static final String TAG = "ChangePasswordFragment";

    private FragmentChangePasswordBinding binding;
    private UserViewModel userViewModel;
    private ProfileViewModel profileViewModel;
    private int containerID;

    public ChangePasswordFragment() {

    }

    public static ChangePasswordFragment newInstance(int containerID) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        profileViewModel = ViewModelFactory.getInstance(getActivity()).create(ProfileViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getChangePassword());
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);

        binding.buttonSubmit.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    private void onClick(View v) {

        if (v.getId() == binding.buttonSubmit.getId()) {

            String oldPassword = binding.editTextOldPassword.getText().toString();
            String newPassword1 = binding.editTextNewPassword1.getText().toString();
            String newPassword2 = binding.editTextNewPassword2.getText().toString();

            if (newPassword1.equals(newPassword2)) {

                userViewModel.getSignInResponse().observe(this, signInResponse -> {

                    if (signInResponse != null) {

                        if (signInResponse.getSuccess() == 0) {
                            profileViewModel.changePassword(getActivity(), signInResponse.getCustomerId(), oldPassword, newPassword1, new NetworkOperations(true));
                            profileViewModel.getChangePasswordResponse().observe(this, changePasswordResponse -> {

                                if (changePasswordResponse != null) {

                                    if (changePasswordResponse.getSuccess() != null && changePasswordResponse.getSuccess().equals(1L)) {
                                        showMessage("Alert", changePasswordResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                                            goBack();
                                            dialogInterface.dismiss();
                                        }, dialogInterface -> {
                                            dialogInterface.dismiss();
                                        });
                                    } else {
                                        showMessage("Alert", changePasswordResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                                            dialogInterface.dismiss();
                                        }, null);
                                    }
                                    profileViewModel.getChangePasswordResponse().removeObservers(this);
                                }

                            });

                        }

                        userViewModel.getSignInResponse().removeObservers(this);

                    }

                });


            } else {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getBOTHPASSWORDSHOULDBESAME());
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String getTAG() {
        return TAG;
    }
}
