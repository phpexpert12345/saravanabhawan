package com.harperskebab.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harperskebab.databinding.FragmentFoodItemExtraToppingBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.model.ToppingDao;
import com.harperskebab.model.ToppingItems;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.network.retrofit.responsemodels.RmGetFoodItemExtraToppingResponse;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.RestaurantFoodItemExtraAdapterInterface;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodItemExtraAdapter;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.viewmodel.FoodItemExtraToppingViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchItemExtraToppingFragment extends BaseFragment{
    private FragmentFoodItemExtraToppingBinding binding;
    List<FoodItemExtraTopping> selected_tops=new ArrayList<>();
    Food food;
    HomeActivity homeActivity;
    double item_price=0.0;
    private FoodItemExtraToppingViewModel foodItemExtraToppingViewModel;
    @Override
    public boolean onBackPressed(View v, int keyCode, KeyEvent event) {
       homeActivity.initiateHomeFragment();
        return false;
    }
    public static SearchItemExtraToppingFragment newInstance(Bundle args){
        SearchItemExtraToppingFragment searchItemExtraToppingFragment=new SearchItemExtraToppingFragment();
        if(args!=null){
            searchItemExtraToppingFragment.setArguments(args);
        }
        return searchItemExtraToppingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodItemExtraToppingViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodItemExtraToppingViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity= (HomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodItemExtraToppingBinding.inflate(inflater, container, false);
        if(getArguments()!=null){
            food=getArguments().getParcelable("food");
            binding.textViewFood.setText(food.getRestaurantPizzaItemName());
            binding.textViewFoodDescription.setText(food.getResPizzaDescription());
            binding.textViewExtraToppingOffer.setVisibility(View.GONE);
            item_price= Double.parseDouble(food.getRestaurantPizzaItemPrice());
            binding.textViewTotalPrice.setText(
                    Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                            + " " +
                            Utility.DECIMAL_FORMAT.format(item_price)
            );
            getExtraTops();
//

        }
        binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTops(selected_tops);
                homeActivity.initiateHomeFragment();
            }
        });
        return binding.getRoot();
    }
    private void SaveTops(List<FoodItemExtraTopping>foods){
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();
        ToppingDao toppingDao=customerAppDatabase.toppingDao();
        StringBuilder tops=new StringBuilder();
        if(foods!=null){
            if(foods.size()>0){
                for(FoodItemExtraTopping food:foods){
                    if(tops.length()>0){
                        tops.append(",");
                    }
                    tops.append(food.getExtraID());
                }
            }
            CartItem cartitem = cartDao.getOrderItemids(
                    food.getRestaurantPizzaItemName()  ,
                    tops.toString()
            );
            if (cartitem != null) {
                UpdateCartItem(cartitem);
            } else {
                AddCartItem(foods);
            }

        }

    }
    private void UpdateCartItem(CartItem cartItem){
        Integer quantity = cartItem.quantity;
        quantity += quantity;
        cartItem.quantity = quantity;
        CustomerAppDatabase cartDatabase = CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao = cartDatabase.cartDao();
        cartDao.Update(cartItem);

    }
    private void AddCartItem(List<FoodItemExtraTopping>foods){
        double total_price=0.0;
        double toppings_sum=0.0;
        double price= Double.parseDouble(food.getRestaurantPizzaItemPrice());
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();
        ToppingDao toppingDao=customerAppDatabase.toppingDao();
        StringBuilder tops=new StringBuilder();
        StringBuilder top_name=new StringBuilder();
        StringBuilder top_price=new StringBuilder();
        CartItem cartItem=new CartItem();
        cartItem.item_name=food.getRestaurantPizzaItemName();
        cartItem.item_id=food.getItemID();
        cartItem.deal_id=food.getItemID();
        cartItem.item_size_type=food.getRestaurantPizzaItemSizeName();
        cartItem.item_price=food.getRestaurantPizzaItemPrice();
            cartItem.item_size_id= "0";

        cartItem.quantity=1;
        cartItem.item_price=food.getRestaurantPizzaItemPrice();
        cartItem.com=false;
        cartItem.desc=food.getResPizzaDescription();
        cartItem.top_ids="";
        cartItem.top_name="";
        cartItem.top_price="";
        cartItem.total_price="0.0";
        cartItem.icon=food.getFoodIcon();
        cartItem.food_tax_applicable=food.getFoodTaxApplicable();
        if(foods!=null) {
            if (foods.size() > 0) {
                for (FoodItemExtraTopping food : foods) {
                    double topping_price = Double.parseDouble(food.getFoodPriceAddons());
                    toppings_sum += topping_price;
                    if (tops.length() > 0) {
                        tops.append(",");
                    }
                    if(top_name.length()>0){
                        top_name.append(",");
                    }
                    if(top_price.length()>0){
                        top_price.append(",");
                    }
//                    ToppingItems toppingItems=new ToppingItems();
//                    toppingItems.topping_id=food.getExtraID();
//                    toppingItems.item_id= String.valueOf(selectedfood.getItemID());
//                    toppingItems.item_name=selectedfood.getRestaurantPizzaItemName();
//                    toppingItems.topping_name=food.getFoodAddonsName();
//                    toppingItems.topping_price=food.getFoodPriceAddons();
//                    toppingDao.Insert(toppingItems);
                    top_name.append(food.getFoodAddonsName());
                    top_price.append(food.getFoodPriceAddons());
                    total_price=price+toppings_sum;
                    tops.append(food.getExtraID());
                }
                List<ToppingItems>topItems=toppingDao.getToppingsbyItem();
                Log.i("log",topItems.size()+"");
            }



        }
        if(total_price>0.0){
            cartItem.total_price = String.valueOf(total_price);
            if(tops.length()>0){
                cartItem.top_ids=tops.toString();
            }
            if(top_name.length()>0){
                cartItem.top_name=top_name.toString();
            }
            if(top_price.length()>0){
                cartItem.top_price=top_price.toString();
            }
            cartDao.Insert(cartItem);
        }
        else{
            cartItem.total_price = String.valueOf(price);
            if(tops.length()>0){
                cartItem.top_ids=tops.toString();
            }
            if(top_name.length()>0){
                cartItem.top_name=top_name.toString();
            }
            if(top_price.length()>0){
                cartItem.top_price=top_price.toString();
            }
            cartDao.Insert(cartItem);
        }

    }
    private void getExtraTops(){
        NetworkOperations call=  new NetworkOperations(true);
        call.onStart(getContext(),"");
        String url=Constant.Url.BASE_URL+"phpexpert_food_items_extra.php?api_key="+Constant.API.FOOD_KEY+"&lang_code="+Constant.API.LANGUAGE_CODE+"&ItemID="+food.getItemID()+"&FoodItemSizeID=";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                call.onComplete();
if(response!=null){
    Log.i("res",response);
    try {
        JSONObject jsonObject=new JSONObject(response);
        if(jsonObject.has("Menu_ItemExtraGroup")){
            JSONArray jsonArray=jsonObject.getJSONArray("Menu_ItemExtraGroup");
            if(jsonArray.length()>0){
                JSONArray items=jsonArray.getJSONArray(0);
                Gson gson=new Gson();
                Type type=new TypeToken<List<RmGetFoodItemExtraToppingResponse.MenuItemExtraGroup>>(){}.getType();
                List<RmGetFoodItemExtraToppingResponse.MenuItemExtraGroup>menuItemExtraGroups=gson.fromJson(items.toString(),type);
                if(menuItemExtraGroups.get(0).getFoodAddonsSelectionType().equalsIgnoreCase("Checkbox")){
                    binding.textViewSizeTitle.setVisibility(View.VISIBLE);
                }
                else{
                    binding.textViewSizeTitle.setVisibility(View.GONE);
                }
                SetAdapter(menuItemExtraGroups);
            }
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                call.onComplete();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void SetAdapter(List<RmGetFoodItemExtraToppingResponse.MenuItemExtraGroup>list){
        FoodItemExtraAdapter foodItemExtraAdapter=new FoodItemExtraAdapter(list, list.get(0).getFoodItemExtraTopping(), new RestaurantFoodItemExtraAdapterInterface() {
            @Override
            public void getTop(int pos, FoodItemExtraTopping foodItemExtraTopping) {
                int selected=-1;
                if(selected_tops.size()>0){
                    for(FoodItemExtraTopping food:selected_tops){
                        if(food.getExtraID().equals(foodItemExtraTopping.getExtraID())){
                            selected=selected_tops.indexOf(food);
                            break;
                        }
                    }

                }
                if(selected>=0){
                    selected_tops.remove(selected);
                }
                if(foodItemExtraTopping.isChecked()) {

                    selected_tops.add(foodItemExtraTopping);
                }
                Double price = 0.0;
                for (int i = 0; i < selected_tops.size(); i++) {
                    FoodItemExtraTopping extraTopping = selected_tops.get(i);
                    price += Double.parseDouble(extraTopping.getFoodPriceAddons());
                }
                item_price+=price;
                binding.textViewTotalPrice.setText(
                        Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                                + " " +
                                Utility.DECIMAL_FORMAT.format(item_price)
                );
            }

            @Override
            public void getCheckedItem(String item, String price, long id, List<FoodItemExtraTopping> list) {
                int added=-1;
                if(selected_tops.size()>0){
                    for (int i = 0; i < selected_tops.size(); i++) {
                        FoodItemExtraTopping extraTopping = selected_tops.get(i);
                        if(id==extraTopping.getExtraID()){
                            added=i;
                            break;
                        }

                    }
                    if(added>=0){
                        selected_tops.remove(added);
                        added=-1;
                        FoodItemExtraTopping extraTopping=new FoodItemExtraTopping();
                        extraTopping.setExtraID(id);
                        extraTopping.setFoodAddonsName(item);
                        extraTopping.setFoodPriceAddons(price);
                        selected_tops.add(extraTopping);
                    }
                    for(int i=0;i< list.size();i++){
                        for(int j=0;j<selected_tops.size();j++){
                            if(list.get(i).getExtraID().equals(selected_tops.get(j).getExtraID())){
                                added=j;
                                break;
                            }
                        }

                    }
                    if(added>=0){
                        selected_tops.remove(added);
                        added=-1;
                        FoodItemExtraTopping extraTopping=new FoodItemExtraTopping();
                        extraTopping.setExtraID(id);
                        extraTopping.setFoodAddonsName(item);
                        extraTopping.setFoodPriceAddons(price);
                        selected_tops.add(extraTopping);
                    }

                }
                else{
                    FoodItemExtraTopping extraTopping=new FoodItemExtraTopping();
                    extraTopping.setExtraID(id);
                    extraTopping.setFoodAddonsName(item);
                    extraTopping.setFoodPriceAddons(price);
                    selected_tops.add(extraTopping);
                }
                Double price1 = 0.0;
                for (int i = 0; i < selected_tops.size(); i++) {
                    FoodItemExtraTopping extraTopping = selected_tops.get(i);
                    price1 += Double.parseDouble(extraTopping.getFoodPriceAddons());
                }
                item_price+=price1;
                binding.textViewTotalPrice.setText(
                        Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                                + " " +
                                Utility.DECIMAL_FORMAT.format(item_price)
                );

            }
        },Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.recyclerViewFoodItemExtraTopping.setAdapter(foodItemExtraAdapter);
        binding.recyclerViewFoodItemExtraTopping.setLayoutManager(linearLayoutManager);
    }
}
