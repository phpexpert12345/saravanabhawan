package com.harperskebab.view.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.harperskebab.R;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class SplashActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        userViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(UserViewModel.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userViewModel.getSignInResponse().getValue()!=null){
                    if(userViewModel.getSignInResponse().getValue().getSuccess()==0){
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                    }
                    else{
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }

                    finish();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        },2500);
    }
}
