package com.harperskebab.view.ui.fragments;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.harperskebab.R;
import com.harperskebab.databinding.DialogNoOfPersonBinding;
import com.harperskebab.databinding.FragmentTableSelectionBinding;
import com.harperskebab.model.AddressFromPostcode;
import com.harperskebab.model.Food;
import com.harperskebab.model.Table;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.view.adapter.AddressFromPostcodeAdapter;
import com.harperskebab.view.adapter.TableAdapter;
import com.harperskebab.view.ui.activities.MapActivity;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.EatInViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class TableSelectionFragment extends BaseFragment {
    private static final String TAG = "BookATableFragment";
    List<String> listClone;
    List<Table> arrTables;
    List<Table> arrTablesFilter;
    private FragmentTableSelectionBinding binding;
    private CartViewModel cartViewModel;
    private FoodViewModel foodViewModel;
    private EatInViewModel eatInViewModel;

    private String selectedTableID = null;
    private Table selectedTable = null;
    private int noOfPerson = 0;
    ArrayList<String> arrTableNo;
    private int containerID;
    String BranchId;

    public TableSelectionFragment() {
    }

    public static TableSelectionFragment newInstance(int containerID, String tableID) {

        TableSelectionFragment fragment = new TableSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putString(Constant.Data.SELECTED_TABLE_ID, tableID);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            selectedTableID = getArguments().getString(Constant.Data.SELECTED_TABLE_ID);
        }

        cartViewModel = ViewModelFactory.getInstance(getActivity()).create(CartViewModel.class);
        foodViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodViewModel.class);
        eatInViewModel = ViewModelFactory.getInstance(getActivity()).create(EatInViewModel.class);
        BranchId=  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BranchId","2");
        eatInViewModel.getTableList(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true),BranchId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getSelectTableNumber());
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getChooseTable());
        binding = FragmentTableSelectionBinding.inflate(inflater, container, false);
binding.textViewChooseTable.setText(languageViewModel.getLanguageResponse().getValue().getYOU_ARE_CURRENTLY_HERE());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String branchName = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BRANCH_NAME", "");
        String branchAddress = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("BRANCH_ADDRESS", "");
        binding.txtAddress.setText(branchAddress);
        binding.txtBranchName.setText(branchName);
        binding.edtSearchTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                arrTablesFilter= new ArrayList<>();
                if (value.length() > 0) {
                    for (int i = 0; i < arrTables.size(); i++) {
                        if (arrTables.get(i).getTableNumber().contains(value)) {
                            Table table=new Table();
                            table.setTableNumber(arrTables.get(i).getTableNumber());
                            table.setId(arrTables.get(i).getId());

                            arrTablesFilter.add(table);
                        }
                    }
                    if (arrTablesFilter.size()>0) {
                        TableAdapter tableAdapter = new TableAdapter(getActivity(), arrTablesFilter, Long.decode(selectedTableID), (position, table) -> {
                            showDialogForNoOfPersonConfirmation(table);
                        });
                        binding.recyclerView.setAdapter(tableAdapter);
                        binding.layNoItemFound.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }else {
                        binding.layNoItemFound.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    TableAdapter tableAdapter = new TableAdapter(getActivity(), arrTables, Long.decode(selectedTableID), (position, table) -> {
                        showDialogForNoOfPersonConfirmation(table);
                    });
                    binding.recyclerView.setAdapter(tableAdapter);
                    binding.layNoItemFound.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

        });
        eatInViewModel.getTableList().observe(this, tables -> {
            if (tables != null) {
                arrTables = tables;
                TableAdapter tableAdapter = new TableAdapter(getActivity(), arrTables, Long.decode(selectedTableID), (position, table) -> {
                    showDialogForNoOfPersonConfirmation(table);
                });

                binding.recyclerView.setAdapter(tableAdapter);

                for (Table table : tables) {
                    if (table.getId().equals(selectedTableID)) {
                        showDialogForNoOfPersonConfirmation(table);
                        break;
                    }
                }

                eatInViewModel.getTableList().removeObservers(this);
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

    }
     int noPerson;
    private void showDialogForNoOfPersonConfirmation(Table table) {

        DialogNoOfPersonBinding dialogNoOfPersonBinding = DialogNoOfPersonBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogNoOfPerson = new BottomSheetDialog(getActivity(), R.style.AppTheme_Transparent);
        dialogNoOfPerson.setContentView(dialogNoOfPersonBinding.getRoot());

        dialogNoOfPersonBinding.edtNoOfPerson.setText(table.getNumberOfPeople());
        dialogNoOfPersonBinding.buttonSubmit.setText(languageViewModel.getLanguageResponse().getValue().getSubmit());
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
                else{
                    dialogNoOfPerson.dismiss();
                }
            }
        });
        dialogNoOfPersonBinding.buttonSubmit.setOnClickListener(v -> {
            int noOfPerson = Integer.parseInt(dialogNoOfPersonBinding.edtNoOfPerson.getText().toString());
            if (Integer.parseInt(table.getNumberOfPeople()) < noOfPerson) {
                PopMessage.makeLongToast(getActivity(), getString(R.string.selected_table_has_less_capacity));
            } else {
                this.selectedTable = table;
                this.noOfPerson = noOfPerson;
                cartViewModel.getSelectedTableID().setValue(table.getId().toString());
                cartViewModel.getNumberOfPeople().setValue("" + noOfPerson);
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("TableNumber",table.getTableNumber() + " " + table.getTableName()).apply();
                cartViewModel.setOrderType("EAT-IN");
                dialogNoOfPerson.dismiss();
                goBack();
//                onPlusFood();
            }
        });

        dialogNoOfPerson.setTitle(getString(R.string.confirmation));
        dialogNoOfPerson.show();

    }

    private void onPlusFood() {
        if (foodViewModel.getCurrentSelectedFood().getValue().getFoodCount() == 0
                && (foodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")
                || foodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES"))) {
            if (foodViewModel.getCurrentSelectedFood().getValue().getSizeavailable().equalsIgnoreCase("YES")) {
                initiateFoodItemFragment();
            } else if (foodViewModel.getCurrentSelectedFood().getValue().getExtraavailable().equalsIgnoreCase("YES")) {
                initiateFoodItemExtraToppingFragment();
            }
        } else {
            foodViewModel.getCurrentSelectedFood().getValue().setFoodCount(foodViewModel.getCurrentSelectedFood().getValue().getFoodCount() + 1);
            Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
            if (!selectedFoods.contains(foodViewModel.getCurrentSelectedFood().getValue())) {
                selectedFoods.add(foodViewModel.getCurrentSelectedFood().getValue());
            }
            foodViewModel.setSelectedFoods(selectedFoods);
        }
    }

    private void initiateFoodItemFragment() {
        FoodItemFragment foodItemFragment = FoodItemFragment.newInstance(containerID);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, foodItemFragment);
        transaction.addToBackStack(FoodItemFragment.getTAG());
        transaction.commit();
    }

    private void initiateFoodItemExtraToppingFragment() {
        FoodItemExtraToppingFragment foodItemExtraToppingFragment = FoodItemExtraToppingFragment.newInstance(containerID, null);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, foodItemExtraToppingFragment);
        transaction.addToBackStack(FoodItemExtraToppingFragment.getTAG());
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