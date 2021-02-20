package com.harperskebab.view.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.harperskebab.R;
import com.harperskebab.utils.Utility;
import com.harperskebab.viewmodel.FoodCategoryViewModel;
import com.harperskebab.viewmodel.FrontBannerViewModel;
import com.harperskebab.viewmodel.LanguageViewModel;
import com.harperskebab.viewmodel.LocationViewModel;
import com.harperskebab.viewmodel.RestaurantViewModel;
import com.harperskebab.viewmodel.SplashViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class BaseActivity extends AppCompatActivity {

    protected RestaurantViewModel restaurantViewModel;
    protected LanguageViewModel languageViewModel;

    protected LocationViewModel locationViewModel;
    protected SplashViewModel splashViewModel;
    protected FrontBannerViewModel frontBannerViewModel;
    protected FoodCategoryViewModel foodCategoryViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        restaurantViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(RestaurantViewModel.class);
        languageViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(LanguageViewModel.class);
        locationViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(LocationViewModel.class);
        frontBannerViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FrontBannerViewModel.class);
        foodCategoryViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FoodCategoryViewModel.class);

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }
    }

    protected static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    protected void showMessage(String title, String message, String positiveButtonText, String negativeButtonText, Utility.OnPositiveClick onPositiveClick, Utility.OnNegativeClick onNegativeClick) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, (DialogInterface dialogInterface, int i) -> {
            onPositiveClick.onClick(dialogInterface);

        });

        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText, (DialogInterface dialogInterface, int i) -> {
                onNegativeClick.onClick(dialogInterface);
            });
        }

        builder.setCancelable(false);
        builder.show();
    }

    protected void goHome(Context context) {
        startActivity(
                new Intent(context, HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
        finish();

    }


}
