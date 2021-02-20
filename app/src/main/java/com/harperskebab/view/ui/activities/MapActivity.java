package com.harperskebab.view.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.harperskebab.R;
import com.harperskebab.model.AddressFromPostcode;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.AddressFromPostcodeAdapter;
import com.harperskebab.viewmodel.CartViewModel;
import com.harperskebab.viewmodel.RestaurantViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, PlaceSelectionListener {
    FrameLayout fm_map;
    private AutoCompleteTextView locationSearchActv;
    private GoogleMap mMap;
    private ImageView img_back_about;
    MaterialTextView txtDelivery, txtPickup, txtEatIn, txtViewMenuName;
    View viewOne, viewTwo;
    Location currentLocation;
    AppCompatImageView imageViewAppIconMap;
    LinearLayout layOrderType, layPostCode;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    EditText edtPostCode, edtPostCodeClick;
    String strOrderType = "Delivery";
    TextView txtSkip;
    double strLong, strLat;
    double currentLat, currentLong;
    RecyclerView recPostcode;
    ArrayList<String> arrTestModel;
    AddressFromPostcode addressFromPostcode;
    double latitude, longitude;
    String strAddress, strPostCode;
    boolean isClick = false;
    LinearLayout layViewMenuForDelivery;
    private final static int FINE_LOCATION = 100;
    private final static int PLACE_PICKER_REQUEST = 2;
    private static final String TAG = MapActivity.class.getSimpleName();
    private CartViewModel cartViewModel;
//    public  void onClick(String val) {
//        layPostCode.setVisibility(View.GONE);
//        recPostcode.setVisibility(View.GONE);
//        strAddress=val;
//        edtPostCode.setText("");
//        onMapSet(latitude, longitude);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        cartViewModel = ViewModelFactory.getInstance(this).create(CartViewModel.class);
        restaurantViewModel.getRestrurentInformation(MapActivity.this, Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE, new NetworkOperations(true));
        IdS();

//        txtDelivery.setText(languageViewModel.getLanguageResponse().getValue().getDelivery());
//        txtEatIn.setText(languageViewModel.getLanguageResponse().getValue().getEATIN());
//        txtPickup.setText(languageViewModel.getLanguageResponse().getValue().getPickup());
        if(restaurantViewModel.getRestaurant().getValue()!=null) {
            if (restaurantViewModel.getRestaurant().getValue().getHomeDeliveryAvailable().equalsIgnoreCase("Yes")) {
                txtDelivery.setVisibility(View.VISIBLE);
                viewOne.setVisibility(View.VISIBLE);
            } else {
                txtDelivery.setVisibility(View.GONE);
                viewOne.setVisibility(View.GONE);
                txtPickup.setBackgroundResource(R.drawable.left_round_red_bg);
                txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
                txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));
            }
        }
        else{
            txtDelivery.setVisibility(View.GONE);
            viewOne.setVisibility(View.GONE);
            txtPickup.setBackgroundResource(R.drawable.left_round_red_bg);
            txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
            txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
            txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));
        }
        if (restaurantViewModel.getRestaurant().getValue().getPickupAvailable().equalsIgnoreCase("Yes")) {
            txtPickup.setVisibility(View.VISIBLE);
            viewTwo.setVisibility(View.VISIBLE);
        } else {
            txtPickup.setVisibility(View.GONE);
            viewTwo.setVisibility(View.GONE);
        }
        if (restaurantViewModel.getRestaurant().getValue().getDineInAvailable().equalsIgnoreCase("Yes")) {
            txtEatIn.setVisibility(View.VISIBLE);
            viewTwo.setVisibility(View.VISIBLE);
        } else {
            txtEatIn.setVisibility(View.GONE);
            viewTwo.setVisibility(View.GONE);
            txtPickup.setBackgroundResource(R.drawable.left_round_red_bg);
            txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
            txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
            txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));
        }

        SharedPreferences preferencess = PreferenceManager.getDefaultSharedPreferences(this);
        strOrderType = preferencess.getString("ORDERTYPE", "Delivery");
        if (!strOrderType.equals("")) {
            if (strOrderType.equalsIgnoreCase(getString(R.string.delivery))) {
//                txtViewMenuName.setText("VIEW MENU FOR "+languageViewModel.getLanguageResponse().getValue().getDelivery()+"   ➔");
                txtViewMenuName.setText("VIEW MENU FOR DELIVERY   ➔");
                txtDelivery.setBackgroundResource(R.drawable.left_round_red_bg);
                txtPickup.setBackgroundResource(R.color.colorWhite);
                txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                txtDelivery.setTextColor(getResources().getColor(R.color.colorWhite));
                txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));

            } else if (strOrderType.equalsIgnoreCase(getString(R.string.pick_up))) {
//                txtViewMenuName.setText("VIEW MENU FOR "+languageViewModel.getLanguageResponse().getValue().getPickup()+"   ➔");
                txtViewMenuName.setText("VIEW MENU FOR PICKUP   ➔");
                txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                txtPickup.setBackgroundColor(getResources().getColor(R.color.otherRed));
                txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
                txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));


            } else if (strOrderType.equalsIgnoreCase(getString(R.string.eat_in))) {
//                txtViewMenuName.setText("VIEW MENU FOR "+languageViewModel.getLanguageResponse().getValue().getEATIN()+"   ➔");
                txtViewMenuName.setText("VIEW MENU FOR EAT-IN   ➔");
                txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                txtPickup.setBackgroundResource(R.color.colorWhite);
                txtEatIn.setBackgroundResource(R.drawable.right_round_red_bg);
                txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                txtEatIn.setTextColor(getResources().getColor(R.color.colorWhite));

            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        restaurantViewModel = ViewModelFactory.getInstance(getApplicationContext()).create(RestaurantViewModel.class);
        restaurantViewModel.getRestaurant().observe(this, restaurant -> {
            Glide.with(MapActivity.this).load(restaurant.getRestaurantLogo()).into(imageViewAppIconMap);
        });

        //fm_map=findViewById(R.id.fm_map);

        edtPostCodeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, AddressFromPostCodeActivity.class);
                intent.putExtra("postcode_auto_API_URL",restaurantViewModel.getRestaurant().getValue().getPostcode_auto_API_URL());
                intent.putExtra("postcode_auto_secret_administration_API_Key",restaurantViewModel.getRestaurant().getValue().getPostcode_auto_complete_key());
                startActivityForResult(intent, 1);
//                if (isClick) {
//                    layPostCode.setVisibility(View.GONE);
//                    recPostcode.setVisibility(View.GONE);
//                    isClick=false;
//                }else {
//                    edtPostCode.setText("");
//                    layPostCode.setVisibility(View.VISIBLE);
//                    isClick=true;
//                }

            }
        });
        cartViewModel.getOrderType().observe(this, orderType -> {
            if (orderType != null) {
                if (orderType.equalsIgnoreCase(getString(R.string.delivery))) {
                    txtViewMenuName.setText("VIEW MENU FOR DELIVERY   ➔");
                    txtDelivery.setBackgroundResource(R.drawable.left_round_red_bg);
                    txtPickup.setBackgroundResource(R.color.colorWhite);
                    txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                    txtDelivery.setTextColor(getResources().getColor(R.color.colorWhite));
                    txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                    txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));

                } else if (orderType.equalsIgnoreCase(getString(R.string.pick_up))) {
                    txtViewMenuName.setText("VIEW MENU FOR PICKUP   ➔");
                    txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                    txtPickup.setBackgroundColor(getResources().getColor(R.color.colorGreenDark));
                    txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                    txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                    txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
                    txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));


                } else if (orderType.equalsIgnoreCase(getString(R.string.eat_in))) {
                    txtViewMenuName.setText("VIEW MENU FOR EAT-IN   ➔");
                    txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                    txtPickup.setBackgroundResource(R.color.colorWhite);
                    txtEatIn.setBackgroundResource(R.drawable.right_round_red_bg);
                    txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                    txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                    txtEatIn.setTextColor(getResources().getColor(R.color.colorWhite));

                }

            } else {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                orderType = preferences.getString("ORDERTYPE", "");
                if (!orderType.equals("")) {
                    if (orderType.equalsIgnoreCase(getString(R.string.delivery))) {
                        txtViewMenuName.setText("VIEW MENU FOR DELIVERY   ➔");
                        txtDelivery.setBackgroundResource(R.drawable.left_round_red_bg);
                        txtPickup.setBackgroundResource(R.color.colorWhite);
                        txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                        txtDelivery.setTextColor(getResources().getColor(R.color.colorWhite));
                        txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                        txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));

                    } else if (orderType.equalsIgnoreCase(getString(R.string.pick_up))) {
                        txtViewMenuName.setText("VIEW MENU FOR PICKUP   ➔");
                        txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                        txtPickup.setBackgroundColor(getResources().getColor(R.color.colorGreenDark));
                        txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                        txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                        txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
                        txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));


                    } else if (orderType.equalsIgnoreCase(getString(R.string.eat_in))) {
                        txtViewMenuName.setText("VIEW MENU FOR EAT-IN   ➔");
                        txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                        txtPickup.setBackgroundResource(R.color.colorWhite);
                        txtEatIn.setBackgroundResource(R.drawable.right_round_red_bg);
                        txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                        txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                        txtEatIn.setTextColor(getResources().getColor(R.color.colorWhite));

                    }
                }
            }
        });
        layViewMenuForDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ORDERTYPE", strOrderType);
                editor.apply();
                if (strOrderType.equalsIgnoreCase("EAT-IN")) {
                    Intent intent = new Intent(MapActivity.this, NewBranchActivity.class);
                    strLat=currentLat;
                    strLong=currentLong;
                    intent.putExtra("LATITUDE", strLat);
                    intent.putExtra("LONGITUDE", strLong);
                    intent.putExtra("CURRENT_LAT", currentLat);
                    intent.putExtra("CURRENT_LONG", currentLong);
                    intent.putExtra("ADDRESS", strAddress);
                    intent.putExtra("TYPE", true);
                    startActivity(intent);
                    finish();
                } else {
                    if (strPostCode == null) {
                        Toast.makeText(MapActivity.this, "Enter Your Postcode", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MapActivity.this, NewBranchActivity.class);
                        if (strLat == 0.0 && strLong == 0.0) {
                            strLat = currentLocation.getLatitude();
                            strLong = currentLocation.getLongitude();
                        }
                        intent.putExtra("LATITUDE", strLat);
                        intent.putExtra("LONGITUDE", strLong);
                        intent.putExtra("CURRENT_LAT", currentLat);
                        intent.putExtra("CURRENT_LONG", currentLong);
                        intent.putExtra("ADDRESS", strAddress);
                        intent.putExtra("TYPE", true);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
        txtDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOrderType = "Delivery";
                txtViewMenuName.setText("VIEW MENU FOR DELIVERY   ➔");
                txtDelivery.setBackgroundResource(R.drawable.left_round_red_bg);
                txtPickup.setBackgroundResource(R.color.colorWhite);
                txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                txtDelivery.setTextColor(getResources().getColor(R.color.colorWhite));
                txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));
            }
        });
        txtPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOrderType = "Pickup";
                txtViewMenuName.setText("VIEW MENU FOR PICKUP   ➔");
                txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                txtPickup.setBackgroundColor(getResources().getColor(R.color.colorGreenDark));
                txtEatIn.setBackgroundResource(R.drawable.right_round_white_bg);
                txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                txtPickup.setTextColor(getResources().getColor(R.color.colorWhite));
                txtEatIn.setTextColor(getResources().getColor(R.color.darkGray));
            }
        });
        txtEatIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOrderType = "EAT-IN";
                txtViewMenuName.setText("VIEW MENU FOR EAT-IN   ➔");
                txtDelivery.setBackgroundResource(R.drawable.left_round_white_bg);
                txtPickup.setBackgroundResource(R.color.colorWhite);
                txtEatIn.setBackgroundResource(R.drawable.right_round_red_bg);
                txtDelivery.setTextColor(getResources().getColor(R.color.darkGray));
                txtPickup.setTextColor(getResources().getColor(R.color.darkGray));
                txtEatIn.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
        edtPostCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                if (value.length() == 7) {
                    final String URL = restaurantViewModel.getRestaurant().getValue().getPostcode_auto_API_URL() + value + "?api-key="+restaurantViewModel.getRestaurant().getValue().getPostcode_auto_complete_key();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    try {
                        JSONObject object = new JSONObject();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response != null) {
                                        VolleyLog.v("Response:%n %s", response.toString(4));
                                        arrTestModel = new ArrayList<>();
                                        addressFromPostcode = new AddressFromPostcode();
                                        latitude = response.optDouble("latitude");
                                        longitude = response.optDouble("longitude");
                                        addressFromPostcode.setLatitude(latitude);
                                        addressFromPostcode.setLongitude(longitude);
                                        JSONArray arr = response.optJSONArray("addresses");
                                        for (int i = 0; i < arr.length(); i++) {
                                            String str = arr.getString(i);
                                            arrTestModel.add(str);
                                        }

                                        AddressFromPostcodeAdapter adapter = new AddressFromPostcodeAdapter(MapActivity.this, arrTestModel);
                                        recPostcode.setAdapter(adapter);
                                        recPostcode.setVisibility(View.VISIBLE);
                                        layPostCode.setVisibility(View.VISIBLE);
                                    } else {
                                        recPostcode.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                recPostcode.setVisibility(View.GONE);
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Intent intent = new Intent(MapActivity.this, AddressFromPostCodeActivity.class);
//                    intent.putExtra("POSTCODE", value);
//                    startActivityForResult(intent, 1);
                } else {
//                    layPostCode.setVisibility(View.GONE);
                    recPostcode.setVisibility(View.GONE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to detect your location!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fm_mappp);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        currentLat = currentLocation.getLatitude();
        currentLong = currentLocation.getLongitude();
//        mMap.setMapType(googleMap.MAP_TYPE_SATELLITE);
        onMapSet(currentLocation.getLatitude(), currentLocation.getLongitude());
//        if (strLat==null&&strLong==null) {
//             latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        }else {
//            double latD = Double.parseDouble(strLat);
//            double longtD = Double.parseDouble(strLong);
//            latLng = new LatLng(latD, longtD);
//        }
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
//        googleMap.addMarker(markerOptions);
//        mMap = googleMap;

//
//
//        LatLng PERTH = new LatLng(Integer.parseInt(strLat),Integer.parseInt(strLong));
//        Marker perth = googleMap.addMarker(
//                new MarkerOptions()
//                        .position(PERTH)
//                        .draggable(true));
//
//        googleMap.setMinZoomPreference(6.0f);
//        googleMap.setMaxZoomPreference(30.0f);
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(PERTH));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(PERTH));
//        //googleMap.getUiSettings().setAllGesturesEnabled(false);
    }

    private void onMapSet(double latitude, double longitude) {
        if (strAddress == null) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                strAddress = address + "," + city + "," + state + "," + country + "-" + postalCode;
//                edtPostCodeClick.setText(postalCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map);
        LatLng latLng = new LatLng(latitude, longitude);
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(str);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
//        mMap.addMarker(markerOptions);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(strAddress)
                .draggable(true)
                .icon(icon));
        marker.showInfoWindow();
//        mMap.setMapType(mMap.);
    }

    public void IdS() {
        imageViewAppIconMap = findViewById(R.id.imageViewAppIconMap);
        txtViewMenuName = findViewById(R.id.txtViewMenuName);
        layOrderType = findViewById(R.id.layOrderType);
        edtPostCode = findViewById(R.id.edtPostCode);
        layPostCode = findViewById(R.id.layPostCode);
        txtDelivery = findViewById(R.id.txtDelivery);
        txtSkip=findViewById(R.id.txtSkip);
        txtEatIn = findViewById(R.id.txtEatIn);
        txtPickup = findViewById(R.id.txtPickup);
        recPostcode = findViewById(R.id.recPostcode);
        recPostcode.setLayoutManager(new LinearLayoutManager(this));
        layViewMenuForDelivery = findViewById(R.id.layViewMenuForDelivery);
        edtPostCodeClick = findViewById(R.id.edtPostCodeClick);
        viewOne = findViewById(R.id.viewTwo);
        viewTwo = findViewById(R.id.viewTwo);
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, NewBranchActivity.class);
                intent.putExtra("LATITUDE", strLat);
                intent.putExtra("LONGITUDE", strLong);
                intent.putExtra("CURRENT_LAT", currentLat);
                intent.putExtra("CURRENT_LONG", currentLong);
//                onMapSet(currentLocation.getLatitude(), currentLocation.getLongitude());
                intent.putExtra("ADDRESS", strAddress);
                intent.putExtra("TYPE", true);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            if (data != null) {
                strAddress = data.getStringExtra("ADDRESS");
                strLat = data.getDoubleExtra("LAT", 0.0);
                strLong = data.getDoubleExtra("LONG", 0.0);
                strPostCode = data.getStringExtra("POSTCODE");
                String response = data.getStringExtra("ADDRESS_FULL");
                edtPostCodeClick.setText(data.getStringExtra("POSTCODE"));
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("POST_CODE", strPostCode).putString("ADDRESS", response).apply();

//                String[] separated = message.split(",");
//                String str1 = separated[0];
//            double latD = Double.parseDouble(strLat);
//            double longtD = Double.parseDouble(strLong);
                onMapSet(strLat, strLong);
//            MarkerOptions marker = new MarkerOptions().position(new LatLng(latD, longtD)).title("point");
//            googleMap.addMarker(marker);
            }
        } else {
//            if (resultCode == RESULT_OK) {
//
//                Place place = PlacePicker.getPlace(this, data);
//
////                placeName.setText(place.getName());
////
////
////                placeAddress.setText(place.getAddress());
//
//
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(), "No place selected", Toast.LENGTH_LONG).show();
//
//            }
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());

                LatLng latLng = place.getLatLng();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        // Display the place's details in the textView.
//        txtPlaceDetails.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
//                place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));
//        txtPlaceDetails.setTextSize(20);

        CharSequence attributions = place.getAttributions();
        if (!TextUtils.isEmpty(attributions)) {
//            txtPlaceAttrib.setText(Html.fromHtml(attributions.toString()));
        } else {
//            txtPlaceAttrib.setText("");
        }
    }

    /*Callback invoked when PlaceAutocomplete encounters an error.*/
    @Override
    public void onError(Status status) {

        Toast.makeText(this, "Place selection was failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
    }
}