package com.harperskebab.view.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.harperskebab.databinding.ActivityPolicyBinding;
import com.harperskebab.utils.Constant;

public class PolicyActivity extends BaseActivity {
    private static final String TAG = "PolicyActivity";

    private ActivityPolicyBinding binding;

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("");

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();

        setupView();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {

        initiateContactUsFragment();

    }

    private void initiateContactUsFragment() {
        String url= Constant.Url.BASE_URL+"privacy_statement_web.php";
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(url);
    }

}