package com.harperskebab.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harperskebab.databinding.FragmentFoodBinding;
import com.harperskebab.databinding.SearchFragmentBinding;
import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.Food;
import com.harperskebab.model.FoodCategory;
import com.harperskebab.model.SearchFoodCategory;
import com.harperskebab.utils.CustomerAppDatabase;
import com.harperskebab.utils.Utility;
import com.harperskebab.view.adapter.FoodAdapter;
import com.harperskebab.view.adapter.FoodCategoryAdapter;
import com.harperskebab.view.adapter.SearchAdapter;
import com.harperskebab.view.ui.activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment{
    SearchFragmentBinding binding;
    List<SearchFoodCategory>foods=new ArrayList<>();
    SearchAdapter searchAdapter;
    FoodAdapter foodAdapter;
    HomeActivity homeActivity;
    int id;
    public static SearchFragment newInstance(Bundle args){
        SearchFragment searchFragment=new SearchFragment();
        if(args!=null){
            searchFragment.setArguments(args);
        }
        return searchFragment;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeActivity= (HomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragmentBinding.inflate(inflater, container, false);
        if(getArguments()!=null){
            foods=getArguments().getParcelableArrayList("category");
            id=getArguments().getInt("id",0);
        }
        if(foods.size()>0){
            setSearchAdapter();
        }
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public boolean onBackPressed(View v, int keyCode, KeyEvent event) {
        homeActivity.initiateHomeFragment();
        return false;
    }
    private void setSearchAdapter(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        searchAdapter=new SearchAdapter(foods, new SearchAdapter.CategoryClicked() {
            @Override
            public void Clicked(int pos) {
                List<Food>foodList=foods.get(pos).subItemsRecord;
                if(foodList.size()>0){
                    setSearchItemAdapter(foodList);
                }
            }
        });
        binding.recylerSearch.setAdapter(searchAdapter);
        binding.recylerSearch.setLayoutManager(linearLayoutManager);

    }
    private void setSearchItemAdapter(List<Food>foodList){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        foodAdapter=new FoodAdapter(getContext(),foodList,onPlusClick,onMinusClick, Utility.getCurrencySymbol(restaurantViewModel.getRestaurant().getValue().getWebsiteCurrencySymbole()));
        binding.recylerSearch.setAdapter(foodAdapter);
        binding.recylerSearch.setLayoutManager(linearLayoutManager);

    }
    FoodAdapter.OnPlusClick onPlusClick= new FoodAdapter.OnPlusClick(){

        @Override
        public void onClick(int position, Food food) {
if(food.getSizeavailable().equalsIgnoreCase("yes")){

}
else if(food.getExtraavailable().equalsIgnoreCase("yes")){
    InitateSearchItemFragment(food);
}
else{
    foodAdapter.foods.get(position).setFoodCount(foodAdapter.foods.get(position).getFoodCount()+1);
    foodAdapter.notifyDataSetChanged();
    SetFoodtoDatabase(2,food);
}

        }
    };
    FoodAdapter.OnMinusClick onMinusClick= new FoodAdapter.OnMinusClick() {
        @Override
        public void onClick(int position, Food food) {
            if(food.getSizeavailable().equalsIgnoreCase("yes")){

            }
            else if(food.getExtraavailable().equalsIgnoreCase("yes")){
InitateSearchItemFragment(food);
            }
            else{
                foodAdapter.foods.get(position).setFoodCount(foodAdapter.foods.get(position).getFoodCount()-1);
                foodAdapter.notifyDataSetChanged();
                SetFoodtoDatabase(0,food);
            }


        }
    };
    private void SetFoodtoDatabase(Integer type,Food food){
        CustomerAppDatabase customerAppDatabase=CustomerAppDatabase.getDatabase(getContext());
        CartDao cartDao=customerAppDatabase.cartDao();

        if(food!=null) {
            CartItem cart = cartDao.getOrderItemids(food.getRestaurantPizzaItemName(), "");
            if (cart != null) {
                Integer quantity = cart.quantity;
                if(quantity==0){
                    cartDao.DeleteCartitem(food.getRestaurantPizzaItemName(), "");
                }
                else {
                    if (type == 0) {


                        quantity -= 1;


                    } else {
                        quantity += 1;
                    }
                    if(quantity==0){
                        cartDao.DeleteCartitem(food.getRestaurantPizzaItemName(), "");
                    }
                    else {
                        cart.quantity = quantity;
                        cartDao.Update(cart);
                    }
                }

            } else {
                CartItem cartItem = new CartItem();
                cartItem.item_id = food.getItemID();
                cartItem.item_name = food.getRestaurantPizzaItemName();
                cartItem.item_size_type = food.getRestaurantPizzaItemSizeName();
                cartItem.item_price = food.getRestaurantPizzaItemPrice();
                cartItem.total_price = food.getRestaurantPizzaItemPrice();
                cartItem.deal_id=food.getItemID();
                cartItem.quantity = 1;
                cartItem.com = false;
                cartItem.top_ids = "";
                cartItem.top_name = "";
                cartItem.top_price = "";
                cartItem.item_size_id = "0";
                cartItem.desc = food.getResPizzaDescription();
                cartItem.icon=food.getFoodIcon();
                cartDao.Insert(cartItem);
            }

        }
        homeActivity.setupBadge();


    }
    private void InitateSearchItemFragment(Food foods){
        Bundle bundle = new Bundle();
        bundle.putParcelable("food",  foods);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(id, SearchItemExtraToppingFragment.newInstance(bundle));
        transaction.addToBackStack(null);
        transaction.commit();
    }




}
