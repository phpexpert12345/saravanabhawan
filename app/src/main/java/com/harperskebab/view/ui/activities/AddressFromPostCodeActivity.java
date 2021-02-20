package com.harperskebab.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.harperskebab.R;
import com.harperskebab.model.AddressFromPostcode;
import com.harperskebab.model.AutoComplete;
import com.harperskebab.model.Suggestion;
import com.harperskebab.view.adapter.AddressFromPostcodeAdapter;
import com.harperskebab.view.adapter.AutocompleteAdapter;
import com.harperskebab.view.adapter.BranchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressFromPostCodeActivity extends BaseActivity {
    ArrayList<String> arrTestModel;
    ArrayList<Suggestion> suggestions;
    AddressFromPostcode addressFromPostcode;
    AutoComplete autoComplete;
    EditText edtPostCode;
    ImageView imgBack;
    double latitude,longitude;
    LinearLayout layout;
    String postcode_auto_API_URL;
    String postcode_auto_secret_administration_API_Key;
    String postalCode;
    Intent intent;
    public  void onClick(String strId, String val) {
        final String URL = "https://api.getaddress.io/get/"+strId+"?api-key="+postcode_auto_secret_administration_API_Key;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response!=null) {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            String strLat=response.getString("latitude");
                            String strLong=response.getString("longitude");
                            String fullPostalCode=response.getString("postcode");
                           String fullAddress=response.getJSONArray("formatted_address").toString();
                            Intent intent=new Intent();
                            intent.putExtra("LAT",Double.parseDouble(strLat));
                            intent.putExtra("LONG",Double.parseDouble(strLong));
                            intent.putExtra("ADDRESS",val);
                            intent.putExtra("ADDRESS_FULL",response.toString());
                            intent.putExtra("POSTCODE",fullPostalCode);
                            setResult(1,intent);
                            finish();//finishing activity
//                            arrTestModel = new ArrayList<>();
//                            addressFromPostcode = new AddressFromPostcode();
//                            latitude = response.optDouble("latitude");
//                            longitude = response.optDouble("longitude");
//                            addressFromPostcode.setLatitude(latitude);
//                            addressFromPostcode.setLongitude(longitude);
//                            JSONArray arr = response.optJSONArray("addresses");
//                            for (int i = 0; i < arr.length(); i++) {
//                                String str = arr.getString(i);
//                                arrTestModel.add(str);
//                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse!=null){
                        if(error.networkResponse.data!=null){
                            String data=new String(error.networkResponse.data);
                            if(data!=null){
                                Log.i("log",data);
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_from_post_code);
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            getWindow().getDecorView().setSystemUiVisibility(3328);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        intent=getIntent();
        if(intent.hasExtra("postcode_auto_API_URL")){
            postcode_auto_API_URL=intent.getStringExtra("postcode_auto_API_URL");
            postcode_auto_secret_administration_API_Key=intent.getStringExtra("postcode_auto_secret_administration_API_Key");
        }
        edtPostCode=findViewById(R.id.edtPostCode);
        imgBack=findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        layout=findViewById(R.id.layout);
        RecyclerView recPostcode = findViewById(R.id.recPostcode);
        recPostcode.setLayoutManager(new LinearLayoutManager(this));
//        String postCode=getIntent().getStringExtra("POSTCODE");
        edtPostCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
//                if (editable.length() > 0 && (editable.length() % 4) == 0) {
//                    final char c = editable.charAt(editable.length() - 1);
//                    if (' ' == c) {
//                        editable.delete(editable.length() - 1, editable.length());
//                    }
//                }
//                if (editable.length() > 0 && (editable.length() % 4) == 0) {
//                    char c = editable.charAt(editable.length() - 1);
//                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf(" ")).length <= 3) {
//                        editable.insert(editable.length() - 1, String.valueOf(" "));
//                    }
//                }
                if (value.length() >1) {
                    postalCode=value;
                    String url=postcode_auto_API_URL+value+"?api-key="+postcode_auto_secret_administration_API_Key+"&TOP=1000";
                    Log.i("url",url);
//                    final String URL = "https://api.getaddress.io/autocomplete/"+value+"?api-key=OspYMXCpvkG568eqQ-tP5Q28829&TOP=1000";
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    try {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response!=null) {
                                        suggestions=new ArrayList<>();
                                        VolleyLog.v("Response:%n %s", response.toString(4));
                                        JSONArray arr=response.getJSONArray("suggestions");
                                        if (arr.length()>0) {
                                            for (int i = 0; i < arr.length(); i++) {
                                                JSONObject obj = arr.getJSONObject(i);
                                                Suggestion suggestion = new Suggestion();
                                                suggestion.setId(obj.getString("id"));
                                                suggestion.setAddress(obj.getString("address"));
                                                suggestion.setUrl(obj.getString("url"));
                                                suggestions.add(suggestion);
                                            }
                                            AutocompleteAdapter autocompleteAdapter = new AutocompleteAdapter(AddressFromPostCodeActivity.this, suggestions);
                                            recPostcode.setAdapter(autocompleteAdapter);
                                            recPostcode.setVisibility(View.VISIBLE);
                                            layout.setVisibility(View.VISIBLE);
                                            recPostcode.setBackgroundResource(R.drawable.white_round_corner_bg);
                                        }else {
                                            layout.setVisibility(View.GONE);
                                        }

//                                        suggestions = new ArrayList<>();
//                                        autoComplete = new AutoComplete();
//                                        autoComplete.set(latitude);
//                                        addressFromPostcode.setLongitude(longitude);
//                                        JSONArray arr = response.optJSONArray("addresses");
//                                        for (int i = 0; i < arr.length(); i++) {
//                                            String str = arr.getString(i);
//                                            arrTestModel.add(str);
//                                        }
//
//                                        AddressFromPostcodeAdapter adapter = new AddressFromPostcodeAdapter(AddressFromPostCodeActivity.this, arrTestModel);
//                                        recPostcode.setAdapter(adapter);
//                                        recPostcode.setVisibility(View.VISIBLE);
//                                        layout.setBackgroundResource(R.drawable.white_round_corner_bg);
                                    }else {
                                        recPostcode.setVisibility(View.GONE);
                                        layout.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error.networkResponse!=null){
                                    if(error.networkResponse.data!=null){
                                        String data=new String(error.networkResponse.data);
                                        if(data!=null){
                                            Log.i("log",data);
                                        }
                                    }
                                }
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
                }
//                else if (value.length()==3){
//                    edtPostCode.setText(value+" ");
//                }
                else {
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
    private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
        boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
        for (int i = 0; i < s.length(); i++) { // check that every element is right
            if (i > 0 && (i + 1) % dividerModulo == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }
    private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }
    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }
}