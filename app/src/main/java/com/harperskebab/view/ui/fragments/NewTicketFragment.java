package com.harperskebab.view.ui.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.harperskebab.R;
import com.harperskebab.databinding.FragmentNewTicketBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.viewmodel.NewTicketViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class NewTicketFragment extends BaseFragment {
    private static final String TAG = "NewTicketFragment";

    private FragmentNewTicketBinding binding;

    private UserViewModel userViewModel;
    private NewTicketViewModel newTicketViewModel;
    private int containerID;

    public NewTicketFragment() {
    }

    public static NewTicketFragment newInstance(int containerID) {

        NewTicketFragment fragment = new NewTicketFragment();
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
        newTicketViewModel = ViewModelFactory.getInstance(getActivity()).create(NewTicketViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.new_ticket));
        binding = FragmentNewTicketBinding.inflate(inflater, container, false);
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

            String orderNo = binding.editTextOrderNo.getText().toString();
            String issueType = binding.editTextIssueType.getText().toString();
            String comment = binding.editTextComment.getText().toString();

            if (orderNo.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterOrderNumber());
            } else if (issueType.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterIssueType());
            } else if (comment.equalsIgnoreCase("")) {
                PopMessage.makeLongToast(getActivity(), languageViewModel.getLanguageResponse().getValue().getPleaseEnterComment());
            } else {

                newTicketViewModel.submitContactUs(
                        getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, issueType, orderNo,
                        userViewModel.getSignInResponse().getValue().getUserEmail(), comment, userViewModel.getSignInResponse().getValue().getCustomerId(),
                        new NetworkOperations(true)
                );

                newTicketViewModel.getTicketResponse().observe(this, submitTicketResponse -> {

                    if (submitTicketResponse != null) {

                        if (submitTicketResponse.getError().equals(0L)) {
                            goBack();
                        }
                        PopMessage.makeLongToast(getActivity(), submitTicketResponse.getError_msg());

                        newTicketViewModel.getTicketResponse().removeObservers(this);
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
