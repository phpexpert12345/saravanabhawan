package com.harperskebab.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ToppingDao {
    @Insert()
    public void Insert(ToppingItems toppingItems);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void Update(ToppingItems toppingItems);
    @Query("Select * from topping_items  order by topping_name")
    public List<ToppingItems> getToppingsbyItem();
    @Query("Delete from topping_items where topping_id=:topping_id")
    public void DeleteTopping(Integer topping_id);
    @Query("Delete from topping_items where item_name=:item_name")
    public void DeleteToppingByItemName(String item_name);
    @Query("Delete from topping_items")
    public void DeleteAll();
}
