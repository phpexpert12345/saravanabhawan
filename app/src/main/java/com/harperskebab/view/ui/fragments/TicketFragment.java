package com.harperskebab.view.ui.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.FragmentTicketBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.TicketAdapter;
import com.harperskebab.viewmodel.TicketViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class TicketFragment extends BaseFragment {
    private static final String TAG = "TicketFragment";

    private FragmentTicketBinding binding;

    private UserViewModel userViewModel;
    private TicketViewModel ticketViewModel;
    private TicketAdapter ticketAdapter;
    private int containerID;

    public TicketFragment() {
    }

    public static TicketFragment newInstance(int containerID) {

        TicketFragment fragment = new TicketFragment();
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
        ticketViewModel = ViewModelFactory.getInstance(getActivity()).create(TicketViewModel.class);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getManageTicket());
        binding = FragmentTicketBinding.inflate(inflater, container, false);

        ticketViewModel.getTickets(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, userViewModel.getSignInResponse().getValue().getCustomerId(), new NetworkOperations(true));
        binding.recyclerViewTicket.setLayoutManager(new LinearLayoutManager(getActivity()));

        ticketViewModel.getTicketResponse().observe(this, ticketResponse -> {
            //TODO
            if (ticketResponse != null) {

                if (ticketResponse.getComplaintsHistory().get(0).getError().equals("0")) {

                    binding.linearLayoutContent.setVisibility(View.VISIBLE);
                    binding.emptyView.getRoot().setVisibility(View.GONE);

                    ticketAdapter = new TicketAdapter(getActivity(), ticketResponse.getComplaintsHistory());
                    binding.recyclerViewTicket.setAdapter(ticketAdapter);

                } else {

                    binding.linearLayoutContent.setVisibility(View.GONE);
                    binding.emptyView.getRoot().setVisibility(View.VISIBLE);
                    binding.emptyView.textViewErrorMessage.setText(ticketResponse.getComplaintsHistory().get(0).getError_msg());


                }
            }

        });

        binding.floatingActionButtonAddTicket.setOnClickListener(this::onClick);

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

        if (v.getId() == binding.floatingActionButtonAddTicket.getId()) {
            initiateNewTicketFragment();
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

    private void initiateNewTicketFragment() {
        NewTicketFragment newTicketFragment = NewTicketFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, newTicketFragment);
        transaction.addToBackStack(NewTicketFragment.getTAG());
        transaction.commit();
    }


}
