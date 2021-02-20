package com.harperskebab.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFoodCategory implements Parcelable {
    @SerializedName("category_description")
    public String categoryDescription;
    @SerializedName("Category_ID")
    public Long categoryID;
    @SerializedName("category_img")
    public String categoryImg;
    @SerializedName("category_name")
    public String categoryName;
    @SerializedName("error")
    public Long error;
    @SerializedName("Favorites_display")
    public String favoritesDisplay;
    @SerializedName("id")
    public Long id;
    @SerializedName("restaurant_id")
    public String restaurantId;
    @SerializedName("Combo_Available")
    public String Combo_Available;
    @SerializedName("sc_obj_id")
    public String scObjId;
    @SerializedName("subItemsRecord")
    public List<Food>subItemsRecord;

    protected SearchFoodCategory(Parcel in) {
        categoryDescription = in.readString();
        if (in.readByte() == 0) {
            categoryID = null;
        } else {
            categoryID = in.readLong();
        }
        categoryImg = in.readString();
        categoryName = in.readString();
        if (in.readByte() == 0) {
            error = null;
        } else {
            error = in.readLong();
        }
        favoritesDisplay = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        restaurantId = in.readString();
        Combo_Available = in.readString();
        scObjId = in.readString();
        subItemsRecord = in.createTypedArrayList(Food.CREATOR);
    }

    public static final Creator<SearchFoodCategory> CREATOR = new Creator<SearchFoodCategory>() {
        @Override
        public SearchFoodCategory createFromParcel(Parcel in) {
            return new SearchFoodCategory(in);
        }

        @Override
        public SearchFoodCategory[] newArray(int size) {
            return new SearchFoodCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryDescription);
        if (categoryID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryID);
        }
        dest.writeString(categoryImg);
        dest.writeString(categoryName);
        if (error == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(error);
        }
        dest.writeString(favoritesDisplay);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(restaurantId);
        dest.writeString(Combo_Available);
        dest.writeString(scObjId);
        dest.writeTypedList(subItemsRecord);
    }
}
