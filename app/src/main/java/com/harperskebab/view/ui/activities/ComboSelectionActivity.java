package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.harperskebab.R;
import com.harperskebab.databinding.ActivityComboSelectionBinding;
import com.harperskebab.model.ComboList;
import com.harperskebab.model.ComboSectionList;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.ComboFoodAdapter;
import com.harperskebab.view.adapter.CooseComboAdapter;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.List;

public class ComboSelectionActivity extends BaseActivity {
    private ActivityComboSelectionBinding binding;
    private FoodViewModel foodViewModel;
    ComboList food;
    int itemPosition;
    private CooseComboAdapter cooseComboAdapter;
    String strTitleName,strTitleDiscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComboSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.choose_combo));
        foodViewModel = ViewModelFactory.getInstance(this).create(FoodViewModel.class);
        strTitleName=getIntent().getStringExtra("ITEM_NAME");
        strTitleDiscription=getIntent().getStringExtra("ITEM_DISCRIPTION");
        itemPosition=getIntent().getIntExtra("ITEM_POSITION",-1);
        binding.txtTitle.setText(strTitleName);
        binding.txtDescription.setText(strTitleDiscription);
        binding.recComboItem.setLayoutManager(new LinearLayoutManager(this));
        binding.recComboItem.setNestedScrollingEnabled(false);
        cooseComboAdapter = new CooseComboAdapter(this,strTitleName,strTitleDiscription,itemPosition, foodViewModel.getGetComboListResponse().getValue().getComboList().get(itemPosition).getComboSectionList());
        binding.recComboItem.setAdapter(cooseComboAdapter);
    }
}