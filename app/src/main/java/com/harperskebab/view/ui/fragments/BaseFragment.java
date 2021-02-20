package com.harperskebab.view.ui.fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.harperskebab.utils.Utility;
import com.harperskebab.viewmodel.LanguageViewModel;
import com.harperskebab.viewmodel.RestaurantViewModel;
import com.harperskebab.viewmodel.SplashViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    protected SplashViewModel splashViewModel;
    protected RestaurantViewModel restaurantViewModel;
    protected LanguageViewModel languageViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashViewModel = ViewModelFactory.getInstance(getActivity()).create(SplashViewModel.class);
        restaurantViewModel = ViewModelFactory.getInstance(getActivity()).create(RestaurantViewModel.class);
        languageViewModel = ViewModelFactory.getInstance(getActivity()).create(LanguageViewModel.class);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(this::onBackPressed);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public abstract boolean onBackPressed(View v, int keyCode, KeyEvent event);

    public void goBack() {
        int backStackEntryCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount <= 1) {
            getActivity().finish();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    protected void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }

    protected void showMessage(String title, String message, String positiveButtonText, String negativeButtonText, Utility.OnPositiveClick onPositiveClick, Utility.OnNegativeClick onNegativeClick) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
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

    protected void setValueInTextView(RelativeLayout parent, TextView title, String titleValue, TextView titlePrice, double value, String currency) {

        if (value != 0D) {
            parent.setVisibility(View.VISIBLE);
            title.setText(titleValue);
            titlePrice.setText(
                    String.format("%s %s", Utility.getCurrencySymbol(currency), Utility.DECIMAL_FORMAT.format(value))
            );
        } else {
            parent.setVisibility(View.GONE);
        }

    }

    public static String getTAG() {
        return TAG;
    }


}
