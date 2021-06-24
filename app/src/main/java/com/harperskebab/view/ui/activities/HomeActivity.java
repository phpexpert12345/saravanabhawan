package com.harperskebab.view.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.duonavigationdrawer.views.DuoMenuView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harperskebab.R;
import com.harperskebab.databinding.ActivityHomeBinding;
import com.harperskebab.databinding.DialogAuthBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.SearchFoodCategory;
import com.harperskebab.model.ToppingDao;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.PopMessage;
import com.harperskebab.utils.Utility;
import com.harperskebab.utils.Validation;
import com.harperskebab.view.adapter.MenuAdapter;
import com.harperskebab.view.ui.activities.user.ChangePasswordActivity;
import com.harperskebab.view.ui.activities.user.MyProfileActivity;
import com.harperskebab.view.ui.fragments.HomeFragment;
import com.harperskebab.view.ui.fragments.SearchFragment;
import com.harperskebab.viewmodel.LanguageViewModel;
import com.harperskebab.viewmodel.UserViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements DuoMenuView.OnMenuClickListener {
    private static final String TAG = "HomeActivity";
    private String branchId;
    private ActivityHomeBinding binding;
    private ImageView imageViewUser;
    private TextView textViewUser;
    TextView textCartItemCount;
    RelativeLayout layCount;
    private TextView textViewUserFullName;
    private TextView textViewUserMobile;
    boolean isLogin = false;
    private MenuAdapter mMenuAdapter;
    private LinkedList<String> mTitles;
    private FragmentManager fragmentManager = null;
    boolean isCount = false;
    private UserViewModel userViewModel;
    String device_id="";
    int is_pay_later=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.header.toolbar);
        binding.header.cartLayout.imgCart.setOnClickListener(v->{
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        });
        setTitle("");
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorGreenDark));
//        branchId = getIntent().getStringExtra("BRANCH_ID");
        userViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(UserViewModel.class);
        callAPI();
        binding.edtCatSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
if(s.length()>0){

    SearchItems(s.toString());
}
else{
    Utility.hideKeyboard(HomeActivity.this);
    initiateHomeFragment();
}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        restaurantViewModel.getRestaurant().observe(this, restaurant -> {
//            Glide.with(HomeActivity.this).load(restaurant.getRestaurantLogo()).into(binding.header.imageViewAppIcon);
            if (restaurant.getLanguage_Available().equalsIgnoreCase("Yes")){
                binding.header.imgLanguage.setVisibility(View.VISIBLE);
            }else {
                binding.header.imgLanguage.setVisibility(View.GONE);
            }
            if(restaurant.getPayLaterAvailable().equalsIgnoreCase("yes")){
                is_pay_later=1;
            }
            else{
                is_pay_later=0;
            }
        });
       String branchName  = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString("BRANCH_NAME", "");
        String branchAddress  = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString("BRANCH_ADDRESS", "");
//        binding.header.txtBranchId.setText(branchName);
        binding.header.txtBranchName.setText(branchName+" "+branchAddress);
        fragmentManager = getSupportFragmentManager();
        binding.header.txtBranchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(restaurantViewModel.getRestaurant().getValue().getBranch_Available().equalsIgnoreCase("Yes")) {
                    Intent intent = new Intent(HomeActivity.this, NewBranchActivity.class);
                    intent.putExtra("isFromHome", true);
                    startActivityForResult(intent, 34);
                }
                else{
                    Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                    startActivity(intent);
                }
            }
        });
//        if(restaurantViewModel.getRestaurant().getValue()!=null){
//            if(restaurantViewModel.getRestaurant().getValue().getLanguage_Available().equalsIgnoreCase("yes")){
//                binding.header.imgLanguage.setVisibility(View.VISIBLE);
//            }
//            else{
//                binding.header.imgLanguage.setVisibility(View.GONE);
//            }
//
//        }
//        else{
//            binding.header.imgLanguage.setVisibility(View.GONE);
//        }
        binding.header.imgLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(HomeActivity.this, LanguageActivity.class)
                );
            }
        });
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    //initiateHomeFragment();
                    break;
                case R.id.action_menu:
                    Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                    intent.putExtra(Constant.Data.FOOD_CATEGORY_POSITION, 0);
                    startActivity(intent);
                    break;
                case R.id.action_cart:
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    break;
                case R.id.action_more:
                    startActivity(new Intent(HomeActivity.this, MoreActivity.class));
                    break;
                case R.id.action_profile:
                    if (userViewModel.getSignInResponse().getValue() == null) {
                        showAuthDialog(0);
                    } else {
                        startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
                    }
                    break;
            }
            return false;
        });

//        setupView();
    }

    private void callAPI() {
        languageViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(LanguageViewModel.class);

            branchId = PreferenceManager.getDefaultSharedPreferences(this).getString("BranchId", "");

//        splashViewModel.getSplash(HomeActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, "1", new NetworkOperations(false));
        restaurantViewModel.getRestrurentInformation(HomeActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        languageViewModel.getLanguage(HomeActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        frontBannerViewModel.getFrontBanner(HomeActivity.this, branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, Constant.API.SLIDER, new NetworkOperations(false));
        foodCategoryViewModel.getFoodCategory(HomeActivity.this, branchId, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(false));

        languageViewModel.getLanguageResponse().observe(HomeActivity.this, languageResponse -> {

            if (languageResponse != null) {
//                Constant.API.LANGUAGE_CODE = language.getLangCode();
//                languageViewModel.getLanguageResponse().removeObservers(this);
                setupView();
            }

        });
        setupBadge();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_home_menu_option, menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_home_menu_option, menu);
//
//        View count = menu.findItem(R.id.option_cart).getActionView();
//       TextView notifCount = (TextView) count.findViewById(R.id.hotlist_hot);
//        getMenuInflater().inflate(R.menu.activity_home_menu_option, menu);
//
//        final MenuItem menuItem = menu.findItem(R.id.option_cart);
//
//        View actionView = menuItem.getActionView();
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });

        return true;
    }

    public void setupBadge() {
        isCount=true;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        String str = preferences.getString("ITEM_COUNT", "");
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(HomeActivity.this);
        CartDao cartDao=customerAppDatabase.cartDao();
        List<CartItem> cart_items=cartDao.getCartItems();
        if(cart_items.size()>0){
            binding.header.cartLayout.cartBadge.setVisibility(View.VISIBLE);
            binding.header.cartLayout.cartBadge.setText(String.valueOf(cart_items.size()));
        }
        else{
            binding.header.cartLayout.cartBadge.setVisibility(View.GONE);
        }
//        isCount = true;
//        if (!str.equalsIgnoreCase("")) {
//            int itemCount = Integer.parseInt(str);
//            if (itemCount != 0) {
//                textCartItemCount.setText(String.valueOf(itemCount));
//                if (textCartItemCount.getVisibility() != View.VISIBLE) {
//                    textCartItemCount.setVisibility(View.VISIBLE);
//                    ;
//                }
//            } else {
//                textCartItemCount.setText(str);
//                if (textCartItemCount.getVisibility() != View.GONE) {
//                    textCartItemCount.setVisibility(View.GONE);
//                }
//            }
//        } else {
//            if (textCartItemCount.getVisibility() != View.GONE) {
//                textCartItemCount.setVisibility(View.GONE);
//            }
//            textCartItemCount.setText(str);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCount) {
            setupBadge();
        }
      String  branchName  = PreferenceManager.getDefaultSharedPreferences(this).getString("BRANCH_NAME", "");
       String branchAddress  = PreferenceManager.getDefaultSharedPreferences(this).getString("BRANCH_ADDRESS", "");
        binding.header.txtBranchName.setText(branchName+" "+branchAddress);

//        callAPI();
        SharedPreferences preferencess = PreferenceManager.getDefaultSharedPreferences(this);
        String strLangImage = preferencess.getString("LANG_IMAGE", "");
//        Glide.with(HomeActivity.this)
//                .load(strLangImage)
//                .into(binding.header.imgLanguage);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHeaderClicked() {
        //TODO
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {

        mMenuAdapter.setViewSelected(position, true);

        if (userViewModel.getSignInResponse().getValue() == null || !userViewModel.getSignInResponse().getValue().getSuccess().equals(0L)) {

            if (position == 0) {
                showAuthDialog(0);
            } else if (position == 1) {
                showAuthDialog(1);
            } else if (position == 2) {
                startActivity(
                        new Intent(HomeActivity.this, ContactUsActivity.class)
                );
            } else if (position == 3) {
                startActivity(
                        new Intent(HomeActivity.this, AboutUsActivity.class)
                );
            } else if (position == 4) {
                startActivity(
                        new Intent(HomeActivity.this, TermsActivity.class)
                );
            } else if (position == 5) {
                startActivity(
                        new Intent(HomeActivity.this, PolicyActivity.class)
                );
            } else if (position == 6) {

            }
        } else if (userViewModel.getSignInResponse().getValue().getSuccess() == 0) {


            if (position == 0) {
                startActivity(new Intent(HomeActivity.this, MyProfileActivity.class));
            } else if (position == 1) {
                startActivity(
                        new Intent(HomeActivity.this, OrderActivity.class)
                );
            } else if (position == 2) {
                startActivity(new Intent(HomeActivity.this, PayLaterOrderActivity.class));
            } else if (position == 3) {
                startActivity(
                        new Intent(HomeActivity.this, AddressActivity.class)
                );
            } else if (position == 4) {
                startActivity(
                        new Intent(HomeActivity.this, OrderReviewActivity.class)
                );
            } else if (position == 5) {
                startActivity(
                        new Intent(HomeActivity.this, TicketActivity.class)
                );
            } else if (position == 6) {
                startActivity(
                        new Intent(HomeActivity.this, LoyaltyActivity.class)
                );
//            } else if (position == 7) {

            } else if (position == 7) {
                startActivity(
                        new Intent(HomeActivity.this, ChangePasswordActivity.class)
                );
            } else if (position == 8) {
                startActivity(
                        new Intent(HomeActivity.this, ReferActivity.class)
                );
//            }else if (position == 9) {
//                    startActivity(
//                            new Intent(HomeActivity.this, LanguageActivity.class)
//                    );
            } else if (position == 9) {
                startActivity(
                        new Intent(HomeActivity.this, ContactUsActivity.class)
                );
            } else if (position == 10) {
                try{ startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
//                Toast.makeText(this, "Rate App", Toast.LENGTH_SHORT).show();

            } else if (position == 11) {
                showMessage("Alert", "Are you sure want to logout ?", "YES", "NO", dialogInterface -> {
                    dialogInterface.dismiss();
                    userViewModel.setSignInResponse(null);

//                    finish();

                    isLogin = false;
                }, dialogInterface -> {
                    dialogInterface.dismiss();
                });

            }


        }
        binding.drawer.closeDrawer();
    }

    private void atemptSignOut() {
        //todo
    }

    @Override
    public void onFooterClicked() {
        //TODO
    }

    private void setupView() {

        imageViewUser = ((DuoMenuView) binding.drawer.getMenuView()).getHeaderView().findViewById(R.id.imageViewUser);
        textViewUser = ((DuoMenuView) binding.drawer.getMenuView()).getHeaderView().findViewById(R.id.textViewUser);
        textViewUserFullName = ((DuoMenuView) binding.drawer.getMenuView()).getHeaderView().findViewById(R.id.materialTextViewUserName);
        textViewUserMobile = ((DuoMenuView) binding.drawer.getMenuView()).getHeaderView().findViewById(R.id.materialTextViewMobile);

        userViewModel.getSignInResponse().observeForever(signInResponse -> {

            if (signInResponse != null && signInResponse.getSuccess() == 0) {
                isLogin = true;
                if (signInResponse.getUserPhoto().equals("")) {
                    imageViewUser.setImageResource(R.drawable.user);
                } else {
                    Glide.with(HomeActivity.this)
                            .load(signInResponse.getUserPhoto())
                            .placeholder(R.drawable.user)
                            .into(imageViewUser);
                }
                textViewUserFullName.setText(String.format("%s %s", signInResponse.getFirstName(), signInResponse.getLastName()).replace("null", "").replace("  ", " "));
                textViewUserMobile.setText(String.format("%s", signInResponse.getUserPhone()));
//<item>My Account</item>
//        <item>My Orders</item>
//        <item>Pay Later</item>
//        <item>My Address</item>
//        <item>My Reviews</item>
//        <item>My Tickets</item>
//        <item>My Loyalty Points</item>
//        <!-- <item>My Wallet</item>-->
//        <item>Change Password</item>
//        <item>Refer a Friends</item>
//         <item>Language setting</item>
//        <item>Contact Us/Help</item>
//        <item>Rate App</item>
//        <item>Logout</item>
//                mTitles = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptionsUsers)));
//                Glide.with(HomeActivity.this).load(languageViewModel.getLanguageListResponse().getValue().getLanguage().get()).into(binding.header.imgLanguage);
                ArrayList<String> arr = new ArrayList<>();
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyAccount());
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyOrder());


//                if(restaurantViewModel.getRestaurant().getValue().getPayLaterAvailable().equalsIgnoreCase("yes")){
//                    arr.add("Pay Later");
//                }
                arr.add("Pay Later");
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyAddress());
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyReview());
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyTicket());
                arr.add(languageViewModel.getLanguageResponse().getValue().getLoyaltyPoints());
                arr.add(languageViewModel.getLanguageResponse().getValue().getChangePassword());
                arr.add(languageViewModel.getLanguageResponse().getValue().getReferAFriend());
                arr.add(languageViewModel.getLanguageResponse().getValue().getContactUs());
//                if (restaurantViewModel.getRestaurant().getValue().getLanguage_Available().equalsIgnoreCase("Yes")) {
//                    arr.add(languageViewModel.getLanguageResponse().getValue().getLANGUAGE_SETTINGS());
//                }
                arr.add("Rate App");
                arr.add(languageViewModel.getLanguageResponse().getValue().getLogout());
                mTitles = new LinkedList<>(arr);
            } else {
//                if (signInResponse.getUserPhoto().equals("")) {
//
//                } else {
                Glide.with(HomeActivity.this)
                        .load(R.drawable.profile_pic)
                        .placeholder(R.drawable.profile_pic)
                        .into(imageViewUser);
//                }
//                imageViewUser.setImageResource(R.drawable.profile_pic);
                textViewUserFullName.setText("Welcome Guest");
                textViewUserMobile.setText("");
                ArrayList<String> arr = new ArrayList<>();
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyAccount());
                arr.add(languageViewModel.getLanguageResponse().getValue().getMyOrder());
                mTitles = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

            }

            mMenuAdapter = new MenuAdapter(HomeActivity.this, mTitles, isLogin);
            ((DuoMenuView) binding.drawer.getMenuView()).setOnMenuClickListener(HomeActivity.this);
            ((DuoMenuView) binding.drawer.getMenuView()).setAdapter(mMenuAdapter);

        });

        initiateHomeFragment();

//        imageViewUser.setOnClickListener(HomeActivity.this::onClick);

        binding.header.toolbar.setNavigationOnClickListener(v -> {
                    if (binding.drawer.isDrawerOpen()) {
                        binding.drawer.closeDrawer();
                    } else {
                        binding.drawer.openDrawer();
                    }
                }
        );

    }

    private void signIn(String emailID, String password, Dialog dialog ) {
        userViewModel.signIn(HomeActivity.this, emailID, password, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, device_id, "Android", new NetworkOperations(true));
        userViewModel.getSignInResponse().observe(this, signInResponse -> {

            if (signInResponse != null) {
                if (signInResponse.getSuccess() == 0) {

                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", "Close", dialogInterface -> {
                        dialog.dismiss();
                        dialogInterface.dismiss();
                    }, dialogInterface -> {
                        dialogInterface.dismiss();
                    });

                } else {
                    userViewModel.setSignInResponse(null);
                    showMessage("Alert", signInResponse.getSuccessMsg(), "OK", null, dialogInterface -> {
                        dialogInterface.dismiss();
                    }, null);
                }
                userViewModel.getSignInResponse().removeObservers(this);
            }

        });
    }

    private void showAuthDialog(int position) {
        getToken();
        DialogAuthBinding dialogAuthBinding = DialogAuthBinding.inflate(getLayoutInflater());
        BottomSheetDialog dialogAuth = new BottomSheetDialog(HomeActivity.this, R.style.AppTheme_Transparent);
        dialogAuth.setContentView(dialogAuthBinding.getRoot());

        dialogAuthBinding.tabLayoutAuth.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        dialogAuthBinding.linearLayoutSignIn.setVisibility(View.VISIBLE);
                        dialogAuthBinding.linearLayoutSignUp.setVisibility(View.GONE);

                        break;
                    case 1:
                        dialogAuthBinding.linearLayoutSignIn.setVisibility(View.GONE);
                        dialogAuthBinding.linearLayoutSignUp.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dialogAuthBinding.tabLayoutAuth.selectTab(dialogAuthBinding.tabLayoutAuth.getTabAt(position));

        dialogAuthBinding.buttonForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ForgetPasswordActivity.class));
        });

        String regex = "\\d+";

        dialogAuthBinding.buttonSignIn.setOnClickListener(v -> {

            String emailID = dialogAuthBinding.editTextSignInEmail.getText().toString();
            String password = dialogAuthBinding.editTextSignInPassword.getText().toString();

            if (emailID.equals("")) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (password.equals("")) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterPassword());
            } else {

                signIn(emailID, password, dialogAuth);
            }

        });


        dialogAuthBinding.buttonSignUp.setOnClickListener(v -> {

            String fullName = dialogAuthBinding.editTextSignUptFullName.getText().toString();
            String emailID = dialogAuthBinding.editTextSignUpEmail.getText().toString();
            String phone = dialogAuthBinding.editTextSignUpPhone.getText().toString();
            String password = dialogAuthBinding.editTextSignUpPassword.getText().toString();

            int phoneLength = phone.length();

            if (fullName.equals("")) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterFullName());
            }  else if (emailID.equals("")) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterEmailAddress());
            } else if (Validation.isValidEmail(emailID)) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPLEASEENTERVALIDEMAIL());
            } else if (phone.equals("")) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (!phone.matches(regex)) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (phoneLength < 10) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterPhoneNo());
            } else if (password.equals("")) {
                PopMessage.makeLongToast(HomeActivity.this, languageViewModel.getLanguageResponse().getValue().getPleaseEnterPassword());
            } else {
                String firstName, lastname;
                if (fullName.contains(" ")) {
                    firstName = fullName.split(" ")[0];
                    lastname = fullName.substring(fullName.indexOf(' ') + 1);
                } else {
                    firstName = fullName;
                    lastname = "";
                }

                userViewModel.signUp(HomeActivity.this, firstName, lastname, emailID, password, phone, "", "", Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                        device_id, "Android",branchId, new NetworkOperations(true));

                userViewModel.getSignUpResponse().observe(this, signUpResponse -> {

                    if (signUpResponse != null) {
                        if (signUpResponse.getError().equals(1L)) {
                            signIn(emailID, password, dialogAuth);
                        } else {
                            userViewModel.getSignUpResponse().setValue(null);
                            showMessage("Alert", signUpResponse.getErrorMsg(), "OK", null, dialogInterface -> {
                                dialogInterface.dismiss();
                            }, null);
                        }
                        userViewModel.getSignUpResponse().removeObservers(this);
                    }
                });
            }
        });

        dialogAuth.show();
    }
    public void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("reason", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                       device_id = task.getResult();
                        if(device_id!=null){
                            Log.i("log",device_id);

                        }

                        // Log and toast

                    }
                });
    }

    public void initiateHomeFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("BranchId", branchId);
        HomeFragment homefragment = HomeFragment.newInstance(binding.frameContainerHome.getId(), branchId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerHome.getId(), homefragment);
        transaction.commit();
    }

    private void SearchItems(String search){
        NetworkOperations nwCall=new NetworkOperations(true);
        nwCall.onStart(this,"");
        String url=Constant.Url.BASE_URL+"phpexpert_food_items_search.php?api_key="+Constant.API.FOOD_KEY+"&lang_code="+Constant.API.LANGUAGE_CODE+"&branch_id="+"2"+"&Category_Item_Text="+search;
        Log.i("url",url);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                nwCall.onComplete();
                Utility.hideKeyboard(HomeActivity.this);
if(response!=null){
    try {
        JSONObject res=new JSONObject(response);
        List<SearchFoodCategory> food_items = new ArrayList<>();
        if(res.has("Menu_Cat")) {
            JSONArray jsonArray = res.getJSONArray("Menu_Cat");
            if (jsonArray.length() > 0) {
                JSONArray cat = jsonArray.getJSONArray(0);
                if (cat.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SearchFoodCategory>>() {
                    }.getType();
                    food_items.addAll(gson.fromJson(cat.toString(),type));
                }
            }
        }
        if(food_items.size()>0){
            InitateSearchFragment(food_items);
        }
        Log.i("cat",food_items.size()+"");
    } catch (JSONException e) {
        e.printStackTrace();
    }



}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nwCall.onComplete();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void InitateSearchFragment(List<SearchFoodCategory>foods){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("category", (ArrayList<? extends Parcelable>) foods);
        bundle.putInt("id", binding.frameContainerHome.getId());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.frameContainerHome.getId(), SearchFragment.newInstance(bundle));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==34){
                callAPI();
            }
        }
    }
}
