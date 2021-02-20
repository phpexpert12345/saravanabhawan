package com.harperskebab.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "topping_items")
public class ToppingItems {
    @ColumnInfo(name = "topping_name")
    @SerializedName("topping_name")
   public String topping_name;
    @ColumnInfo(name="topping_price")
    @SerializedName("topping_price")
    public  String topping_price ;
    @ColumnInfo(name = "item_name")
    @SerializedName("item_name")
    public String item_name;
    @ColumnInfo(name = "topping_id")
    @SerializedName("topping_id")
    public  long topping_id =0;
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public  Integer id=0;
    @ColumnInfo(name="item_id")
    @SerializedName("item_id")
    public String item_id;
}
