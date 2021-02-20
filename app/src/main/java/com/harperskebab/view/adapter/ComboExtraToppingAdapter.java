package com.harperskebab.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.harperskebab.R;
import com.harperskebab.model.ComboList;
import com.harperskebab.model.ComboSectionValueItems;
import com.harperskebab.model.SubExtraItemsRecord;
import com.harperskebab.view.adapter.viewholders.FoodViewHolder;
import com.harperskebab.view.ui.activities.ComboSelectionActivity;
import com.harperskebab.view.ui.activities.ExtraTopingActivity;

import java.util.List;

public class ComboExtraToppingAdapter extends RecyclerView.Adapter<ComboExtraToppingAdapter.ViewHolder>{
    private Context context;
    private int selectedItemPosition = -1;
    List<SubExtraItemsRecord> subExtraItemsRecords;

    public ComboExtraToppingAdapter(Context context, List<SubExtraItemsRecord> subExtraItemsRecords) {
        this.context = context;
        this.subExtraItemsRecords=subExtraItemsRecords;
    }

    @Override
    public ComboExtraToppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.extra_topping_combo_item, parent, false);
        ComboExtraToppingAdapter.ViewHolder viewHolder = new ComboExtraToppingAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubExtraItemsRecord subExtraItemsRecord = subExtraItemsRecords.get(position);
        holder.checkBoxExtraTopping.setText(subExtraItemsRecord.getFoodAddonsName());
        holder.txtPrice.setText(subExtraItemsRecord.getFoodPriceAddons());
    }


    @Override
    public int getItemCount() {
        return subExtraItemsRecords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialCheckBox checkBoxExtraTopping;
public MaterialTextView txtPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBoxExtraTopping = (MaterialCheckBox) itemView.findViewById(R.id.checkBoxExtraTopping);
            txtPrice=(MaterialTextView)itemView.findViewById(R.id.txtPrice);
        }
    }
}
