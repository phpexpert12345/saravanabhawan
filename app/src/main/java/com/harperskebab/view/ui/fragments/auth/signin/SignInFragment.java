package com.harperskebab.view.ui.fragments.auth.signin;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harperskebab.databinding.FragmentSignInBinding;
import com.harperskebab.view.ui.fragments.BaseFragment;

public class SignInFragment extends BaseFragment {
    private static final String TAG = "SignInFragment";

    private FragmentSignInBinding binding;

    public SignInFragment() {

    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);


        return binding.getRoot();
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
}
