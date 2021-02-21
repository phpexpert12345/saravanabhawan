package com.harperskebab.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "cart_item")
public class CartItem {
    @ColumnInfo(name="item_name")
    @SerializedName("item_name")
   public String item_name;
    @ColumnInfo(name="item_price")
    @SerializedName("item_price")
    public  String item_price;
    @ColumnInfo(name="item_size_type")
    @SerializedName("item_size_type")
    public  String item_size_type;
    @ColumnInfo(name="item_image")
    @SerializedName("item_image")
    public String item_image;
    @ColumnInfo(name="total_price")
    @SerializedName("total_price")
    public  String total_price;
    @ColumnInfo(name="item_size_id")
    @SerializedName("item_size_id")
    public  String item_size_id;
    @ColumnInfo(name="quantity")
    @SerializedName("quantity")
    public  Integer quantity;
    @ColumnInfo(name="id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public  Integer id ;
    @ColumnInfo(name = "com")
    @SerializedName("com")
    public boolean com=false;
    @ColumnInfo(name = "item_id")
    @SerializedName("item_id")
    public  long item_id;
    @ColumnInfo(name = "deal_id")
    @SerializedName("deal_id")
    public  long deal_id;
    @ColumnInfo(name = "top_ids")
    @SerializedName("top_ids")
    public String top_ids="";
    @ColumnInfo(name = "desc")
    @SerializedName("desc")
 public String desc="";
    @ColumnInfo(name = "top_price")
 @SerializedName("top_price")
 public String top_price;
    @ColumnInfo(name = "top_name")
 @SerializedName("top_name")
 public String top_name;
    @ColumnInfo(name = "icon")
 @SerializedName("icon")
 public String icon;
    @ColumnInfo(name="food_tax_applicable")
 @SerializedName("food_tax_applicable")
 public String food_tax_applicable;




}
