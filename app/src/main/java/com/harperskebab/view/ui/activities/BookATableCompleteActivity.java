package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.ActivityBookATableCompleteBinding;
import com.harperskebab.model.Table;
import com.harperskebab.utils.Constant;
import com.harperskebab.viewmodel.BookATableViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class BookATableCompleteActivity extends BaseActivity {

    private ActivityBookATableCompleteBinding binding;

    private BookATableViewModel bookATableViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookATableCompleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        binding.textViewTitle.setText(languageViewModel.getLanguageResponse().getValue().getThankYou());
        binding.textViewMessage1.setText(languageViewModel.getLanguageResponse().getValue().getYourBookingConfirmationHasBeenSuccessfullySendToRestaura());

        savedInstanceState = getIntent().getExtras();

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(Constant.Data.TABLE)) {
                Table table = (Table) savedInstanceState.getSerializable(Constant.Data.TABLE);


                bookATableViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(BookATableViewModel.class);

                bookATableViewModel.getBookATableResponse().observe(this, bookATableResponse -> {

                    if (bookATableResponse != null) {
                        //todo

                        binding.textViewMessage2.setText(bookATableResponse.getBookingNumber());

                        if (bookATableResponse.getTransactionNumber() == null) {
                            binding.textViewMessage3.setVisibility(View.GONE);
                        } else {
                            binding.textViewMessage3.setText(bookATableResponse.getTransactionNumber());
                        }
                        binding.textViewMessage4.setText(String.format(
                                languageViewModel.getLanguageResponse().getValue().getTableNo() + ": %s(%s) at %s %s",
                                table.getTableNumber(),
                                table.getTableName(),
                                bookATableResponse.getBookingDate(),
                                bookATableResponse.getBookingTime()));

                        bookATableViewModel.getBookATableResponse().removeObservers(this);
                    }
                });

                binding.buttonBackToHome.setOnClickListener(this::onClick);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goHome(BookATableCompleteActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View v) {

        if (v.getId() == binding.buttonBackToHome.getId()) {
            goHome(BookATableCompleteActivity.this);
        }
    }


    @Override
    public void onBackPressed() {

    }

}