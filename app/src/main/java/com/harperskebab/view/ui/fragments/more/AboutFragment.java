package com.harperskebab.view.ui.fragments.more;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.harperskebab.databinding.FragmentAboutBinding;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.viewmodel.RestaurantViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

public class AboutFragment extends BaseFragment {
    private static final String TAG = "AboutFragment";

    private FragmentAboutBinding binding;
    private RestaurantViewModel restaurantViewModel;

    private int containerID;

    public AboutFragment() {
    }

    public static AboutFragment newInstance(int containerID) {

        AboutFragment fragment = new AboutFragment();
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
        restaurantViewModel = ViewModelFactory.getInstance(getActivity()).create(RestaurantViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(languageViewModel.getLanguageResponse().getValue().getAboutUs());
        binding = FragmentAboutBinding.inflate(inflater, container, false);

        initiateContactUsFragment();

        return binding.getRoot();
    }

    private void initiateContactUsFragment() {
        NetworkOperations networkOperations=    new NetworkOperations(true);
        networkOperations.onStart(getContext(),"");
        String url= Constant.Url.BASE_URL+"about_us_web.php";
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                networkOperations.onComplete();
            }
        });

        binding.webView.loadUrl(url);

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