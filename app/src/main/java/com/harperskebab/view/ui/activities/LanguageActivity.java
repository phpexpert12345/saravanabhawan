package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.ActivityLanguageBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.LanguageAdapter;
import com.harperskebab.viewmodel.LanguageViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class LanguageActivity extends BaseActivity {
    private static final String TAG = "AboutUsActivity";

    private ActivityLanguageBinding binding;
    private LanguageViewModel languageViewModel;
    private LanguageAdapter languageAdapter;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();

        languageViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(LanguageViewModel.class);

        binding.recyclerViewLanguage.setLayoutManager(new LinearLayoutManager(this));

        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {

        languageViewModel.getLanguageList(this, Constant.API.FOOD_KEY, new NetworkOperations(true));
        languageViewModel.getLanguageListResponse().observe(this, languageListResponse -> {

            if (languageListResponse != null) {
                languageAdapter = new LanguageAdapter(LanguageActivity.this, languageListResponse.getLanguage(), Constant.API.LANGUAGE_CODE, onItemClick);
                binding.recyclerViewLanguage.setAdapter(languageAdapter);

                languageViewModel.getLanguageListResponse().removeObservers(this);
            }

        });

    }

    private LanguageAdapter.OnItemClick onItemClick = (position, language) -> {

        languageViewModel.getLanguage(LanguageActivity.this, Constant.API.FOOD_KEY, language.getLangCode(), new NetworkOperations(true));

        languageViewModel.getLanguageResponse().observe(LanguageActivity.this, languageResponse -> {

            if (languageResponse != null) {
                Constant.API.LANGUAGE_CODE = language.getLangCode();
//                languageViewModel.getLanguageResponse().removeObservers(this);
            }

        });


    };

}