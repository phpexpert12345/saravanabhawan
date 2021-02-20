package com.harperskebab.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void Insert(CartItem cartItem);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void Update(CartItem cartItem);
    @Query("Select * from cart_item order by item_name")
    public List<CartItem> getCartItems();
    @Query("delete from cart_item where item_name=:name and top_ids=:top_ids ")
    public void DeleteCartitem(String name,String top_ids);
    @Query("delete from cart_item")
    public void DeleteAll();
    @Query("select * from cart_item where item_name=:name")
    public CartItem getOrderItem(String name);
    @Query("select * from cart_item where item_name=:name and top_ids=:top_ids")
    public CartItem getOrderItemids(String name,String top_ids);
}
