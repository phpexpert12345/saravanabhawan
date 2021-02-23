package com.harperskebab.view.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harperskebab.databinding.ActivityMainBinding;
import com.harperskebab.model.SplashBanners;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetSplashResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.LocationTracker;
import com.harperskebab.view.ui.fragments.startup.StartupFragment;
import com.harperskebab.viewmodel.FoodCategoryViewModel;
import com.harperskebab.viewmodel.FrontBannerViewModel;
import com.harperskebab.viewmodel.LanguageViewModel;
import com.harperskebab.viewmodel.LocationViewModel;
import com.harperskebab.viewmodel.RestaurantViewModel;
import com.harperskebab.viewmodel.SplashViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private static final int CURRENT_LOCATION_REQUEST = 1001;

    private ActivityMainBinding binding;
    private String branchId;
    private FragmentManager fragmentManager = null;
    private StartupFragment startupFragment = null;

    private LocationViewModel locationViewModel;
    private SplashViewModel splashViewModel;
    private RestaurantViewModel restaurantViewModel;
    private LanguageViewModel languageViewModel;
    private FrontBannerViewModel frontBannerViewModel;
    private FoodCategoryViewModel foodCategoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        branchId = getIntent().getStringExtra("BRANCH_ID");
        fragmentManager = getSupportFragmentManager();
        startupFragment = StartupFragment.newInstance(binding.frameContainerAuth.getId());

        locationViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(LocationViewModel.class);
        splashViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(SplashViewModel.class);
        restaurantViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(RestaurantViewModel.class);
        languageViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(LanguageViewModel.class);
        frontBannerViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FrontBannerViewModel.class);
        foodCategoryViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(FoodCategoryViewModel.class);

        setupView();

    }

    private void setupView() {



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, CURRENT_LOCATION_REQUEST);

        } else {
            callAPI();
            checkLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CURRENT_LOCATION_REQUEST) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                }, CURRENT_LOCATION_REQUEST);

            } else {
                callAPI();
                checkLocation();
            }

        }

    }

    private void checkLocation() {
        LocationTracker.LocationResult locationResult = new LocationTracker.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                if (!(location == null)) {

                    locationViewModel.checkLocation(MainActivity.this, "" + location.getLatitude(), "" + location.getLongitude(), Constant.API.FOOD_KEY, new NetworkOperations(true));
                    locationViewModel.getLocationResponse().observe(MainActivity.this, locationResponse -> {

                        if (locationResponse != null) {
                            Constant.API.FOOD_KEY = locationResponse.getApiKey();
                            Constant.API.LANGUAGE_CODE = locationResponse.getCustomerDefaultLangauge();

//                            callAPI();
                            locationViewModel.getLocationResponse().removeObservers(MainActivity.this);
                        }

                    });
                } else {
                    //todo
                }

            }
        };

        LocationTracker locationTracker = new LocationTracker();
        locationTracker.getLocation(this, locationResult);
    }

    private void callAPI() {

        splashViewModel.getSplash(MainActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "1", new NetworkOperations(false));
        restaurantViewModel.getRestrurentInformation(MainActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        languageViewModel.getLanguage(MainActivity.this, Constant.API.FOOD_KEY, Constant.API.FOOD_KEY, new NetworkOperations(true));
        NetworkOperations opt= new NetworkOperations(true);
        opt.onStart(this,"");
        String Url=Constant.Url.BASE_URL + Constant.Url.SPLASH+"?api_key="+Constant.API.FOOD_KEY+"&lang_code="+Constant.API.LANGUAGE_CODE+"&splash_type=1";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                opt.onComplete();
                Gson gson=new Gson();
                Type type=new TypeToken<RmGetSplashResponse>(){}.getType();
                RmGetSplashResponse rmGetSplashResponse=gson.fromJson(response,type);
                List<SplashBanners> splashBanners=rmGetSplashResponse.getSplashBanners();
                if (splashBanners != null) {
                    if (splashBanners.size() > 0) {
                        if(splashBanners.get(0).getError().equalsIgnoreCase("0")){
                            initiateStartupFragment();
                        }
                        else{
                            if(restaurantViewModel.getRestaurant().getValue()!=null){
                                if(restaurantViewModel.getRestaurant().getValue().getBranch_Available().equalsIgnoreCase("No")){
                                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                }
                                else{
                                    startActivity(new Intent(MainActivity.this,MapActivity.class));
                                }
                            }
                            else{
startActivity(new Intent(MainActivity.this,MapActivity.class));
                            }

                            finish();
                        }
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
opt.onComplete();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
//        splashViewModel.getSplashBanners().observe(this, splashBanners -> {
//            if (splashBanners != null) {
//                if (splashBanners.size() > 0) {
//                    if(splashBanners.get(0).getError().equalsIgnoreCase("0")){
//                        initiateStartupFragment();
//                    }
//                    else{
//                        startActivity(new Intent(this, MapActivity.class));
//                        finish();
//                    }
//                }
//            }
//                });
//        frontBannerViewModel.getFrontBanner(MainActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, Constant.API.SLIDER, new NetworkOperations(false));
//        foodCategoryViewModel.getFoodCategory(MainActivity.this,branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(false));

    }

    private void initiateStartupFragment() {
//        startupFragment = StartupFragment.newInstance(binding.frameContainerAuth.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerAuth.getId(), startupFragment);
        transaction.commit();
    }
}