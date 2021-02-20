package com.harperskebab.view.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.FragmentFoodItemExtraToppingBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodItem;
import com.harperskebab.model.FoodItemExtraTopping;
import com.harperskebab.model.ToppingDao;
import com.harperskebab.model.ToppingItems;
import com.harperskebab.network.NetworkOperations;
import com.harperskebab.utils.Constant;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.RestaurantFoodItemExtraAdapterInterface;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodItemExtraAdapter;
import com.harperskebab.view.adapter.FoodItemExtraToppingAdapter;
import com.harperskebab.viewmodel.FoodItemExtraToppingViewModel;
import com.harperskebab.viewmodel.FoodViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FoodItemExtraToppingFragment extends BaseFragment {
    private static final String TAG = "FoodItemExtraToppingFra";

    private FragmentFoodItemExtraToppingBinding binding;
    List<FoodItemExtraTopping>selected_tops=new ArrayList<>();

    private FoodViewModel foodViewModel;
    private FoodItemExtraToppingViewModel foodItemExtraToppingViewModel;
    private Food selectedfood;
    private FoodItem selectedFoodItem;
    private int containerID;
    private int freeToppingSelectionAllowed;
    double item_price=0.0;
    public FoodItemExtraToppingFragment() {
    }

    public static FoodItemExtraToppingFragment newInstance(int containerID, FoodItem foodItem) {

        FoodItemExtraToppingFragment fragment = new FoodItemExtraToppingFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        args.putSerializable(Constant.Data.FOOD_ITEM, foodItem);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
            selectedFoodItem = (FoodItem) getArguments().getSerializable(Constant.Data.FOOD_ITEM);

        }
        foodViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodViewModel.class);
        foodItemExtraToppingViewModel = ViewModelFactory.getInstance(getActivity()).create(FoodItemExtraToppingViewModel.class);


        selectedfood = foodViewModel.getCurrentSelectedFood().getValue();
        item_price= Double.parseDouble(selectedfood.getRestaurantPizzaItemPrice());


        if (selectedfood.getFoodItem() == null) {
            foodItemExtraToppingViewModel.getFoodItemExtraTopping(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                    "" + selectedfood.getItemID(), "",
                    new NetworkOperations(true));
        } else {
            foodItemExtraToppingViewModel.getFoodItemExtraTopping(getActivity(), Constant.API.FOOD_KEY, Constant.API.LANGUAGE_CODE,
                    "" + selectedfood.getItemID(), "" + selectedfood.getFoodItem().getId(),
                    new NetworkOperations(true));
        }


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoodItemExtraToppingBinding.inflate(inflater, container, false);

        binding.textViewFood.setText(selectedfood.getRestaurantPizzaItemName());
        binding.textViewFoodDescription.setText(selectedfood.getResPizzaDescription());
        binding.textViewExtraToppingOffer.setVisibility(View.GONE);
        binding.recyclerViewFoodItemExtraTopping.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.textViewTotalPrice.setText(
                Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                        + " " +
                        Utility.DECIMAL_FORMAT.format(item_price)
        );
        foodItemExtraToppingViewModel.getFoodItemExtraToppingResponseMutableLiveData().observe(this,rmGetFoodItemExtraToppingResponse -> {
if(rmGetFoodItemExtraToppingResponse!=null){
    if(rmGetFoodItemExtraToppingResponse.getMenuItemExtraGroup().get(0).get(0).getFoodAddonsSelectionType().equalsIgnoreCase("Checkbox")){
        binding.textViewSizeTitle.setVisibility(View.VISIBLE);
    }
    else{
        binding.textViewSizeTitle.setVisibility(View.GONE);
    }
    FoodItemExtraAdapter foodItemExtraAdapter=new FoodItemExtraAdapter(rmGetFoodItemExtraToppingResponse.getMenuItemExtraGroup().get(0), rmGetFoodItemExtraToppingResponse.getMenuItemExtraGroup().get(0).get(0).getFoodItemExtraTopping(), new RestaurantFoodItemExtraAdapterInterface() {
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
    binding.recyclerViewFoodItemExtraTopping.setAdapter(foodItemExtraAdapter);
}
        });

//        foodItemExtraToppingViewModel.getFoodItemExtraTopping().observe(this, foodItemExtraToppings -> {
//
//            if (foodItemExtraToppings != null) {
//
//                if (foodItemExtraToppings.get(0).getFreeToppingSelectionAllowed().equalsIgnoreCase("")) {
//                    freeToppingSelectionAllowed = 0;
//                } else {
//                    freeToppingSelectionAllowed = Integer.parseInt(foodItemExtraToppings.get(0).getFreeToppingSelectionAllowed());
//                }
//
//                if (freeToppingSelectionAllowed == 0 || freeToppingSelectionAllowed == 10000) {
//                    binding.textViewExtraToppingOffer.setVisibility(View.GONE);
//                } else {
//                    binding.textViewExtraToppingOffer.setText("Choose Any " + freeToppingSelectionAllowed + " Topping Free");
//                }
//
//                FoodItemExtraToppingAdapter foodItemExtraToppingAdapter = new FoodItemExtraToppingAdapter(
//                        getActivity(), foodItemExtraToppings, onItemClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
//                );
//                binding.recyclerViewFoodItemExtraTopping.setAdapter(foodItemExtraToppingAdapter);
//            }
//
//        });

        binding.buttonAddToCart.setOnClickListener(this::onClick);

        return binding.getRoot();
    }

    private FoodItemExtraToppingAdapter.OnItemClick onItemClick = (position, foodItemExtraTopping) -> {

        FoodItemExtraToppingAdapter foodItemExtraToppingAdapter = (FoodItemExtraToppingAdapter) binding.recyclerViewFoodItemExtraTopping.getAdapter();

        List<FoodItemExtraTopping> selectedFoodItemExtraToppings = foodItemExtraToppingAdapter.getSelectedFoodItemExtraToppings();

        if (selectedFoodItemExtraToppings.size() > freeToppingSelectionAllowed) {

            Double price = 0.0;
            for (int i = freeToppingSelectionAllowed; i < selectedFoodItemExtraToppings.size(); i++) {
                FoodItemExtraTopping extraTopping = selectedFoodItemExtraToppings.get(i);
                price += Double.parseDouble(extraTopping.getFoodPriceAddons());
            }

            binding.textViewTotalPrice.setText(
                    Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                            + " " +
                            Utility.DECIMAL_FORMAT.format(price)
            );
        } else {
            binding.textViewTotalPrice.setText(
                    Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole())
                            + " " +
                            Utility.DECIMAL_FORMAT.format(0.0)
            );
        }

    };

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

        if (v.getId() == binding.buttonAddToCart.getId()) {

//            FoodItemExtraToppingAdapter adapter = (FoodItemExtraToppingAdapter) binding.recyclerViewFoodItemExtraTopping.getAdapter();
//            foodViewModel.getCurrentSelectedFood().getValue().setFoodItem(selectedFoodItem);
//            foodViewModel.getCurrentSelectedFood().getValue().setFoodItemExtraToppings(adapter.getSelectedFoodItemExtraToppings());
//
//            foodViewModel.getCurrentSelectedFood().getValue().setFoodCount(1);
            SaveTops(selected_tops);
//            Set<Food> selectedFoods = foodViewModel.getSelectedFoods().getValue();
//            selectedFoods.add(foodViewModel.getCurrentSelectedFood().getValue());
//            foodViewModel.setSelectedFoods(selectedFoods);

            goBack();

        }

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
                    selectedfood.getRestaurantPizzaItemName()  ,
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
        double price= Double.parseDouble(selectedfood.getRestaurantPizzaItemPrice());
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();
        ToppingDao toppingDao=customerAppDatabase.toppingDao();
        StringBuilder tops=new StringBuilder();
        StringBuilder top_name=new StringBuilder();
        StringBuilder top_price=new StringBuilder();
        CartItem cartItem=new CartItem();
        cartItem.item_name=selectedfood.getRestaurantPizzaItemName();
        cartItem.item_id=selectedfood.getItemID();
        cartItem.deal_id=selectedfood.getItemID();
        cartItem.item_size_type=selectedfood.getRestaurantPizzaItemSizeName();
        cartItem.item_price=selectedfood.getRestaurantPizzaItemPrice();
        if(selectedFoodItem!=null){
            cartItem.item_size_id= String.valueOf(selectedFoodItem.getFoodItemID());
        }
        else{
            cartItem.item_size_id= "0";
        }
        cartItem.quantity=1;
        cartItem.item_price=selectedfood.getRestaurantPizzaItemPrice();
        cartItem.com=false;
        cartItem.desc=selectedfood.getResPizzaDescription();
        cartItem.top_ids="";
        cartItem.top_name="";
        cartItem.top_price="";
        cartItem.total_price="0.0";
        cartItem.icon=selectedfood.getFoodIcon();
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

}
