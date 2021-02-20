package com.harperskebab.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.radiobutton.MaterialRadioButton;
import com.harperskebab.R;
import com.harperskebab.model.CardList;
import com.harperskebab.model.ComboSectionValueItems;
import com.harperskebab.view.adapter.viewholders.ChooseComboInneritemsViewHolder;
import com.harperskebab.view.ui.activities.ComboSelectionActivity;
import com.harperskebab.view.ui.activities.ExtraTopingActivity;


import java.util.List;

public class CooseComboInerItemsAdapter extends RecyclerView.Adapter<CooseComboInerItemsAdapter.ViewHolder>{
    private Context context;
    private int selectedItemPosition = -1;
    List<ComboSectionValueItems> comboSectionValueItemLists;
    String titleName, titleDiscription;
    int itemPosition;
    String strFreeAllowedValue,strComboslotId;
    public CooseComboInerItemsAdapter(Context context,String strComboslotId,String titleName, String titleDiscription,int itemPosition,String strFreeAllowedValue, List<ComboSectionValueItems> comboSectionValueItemLists) {
        this.context = context;
        this.comboSectionValueItemLists = comboSectionValueItemLists;
        this.titleName = titleName;
        this.strComboslotId=strComboslotId;
        this.titleDiscription = titleDiscription;
        this.itemPosition=itemPosition;
        this.strFreeAllowedValue=strFreeAllowedValue;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.selectioncombo_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ComboSectionValueItems comboSectionValueItems = comboSectionValueItemLists.get(position);
        holder.radioButtonInerItemNameasas.setText(comboSectionValueItems.getCombo_Slot_ItemName());
        if (selectedItemPosition==position){
            holder.radioButtonInerItemNameasas.setChecked(true);
        }else {
            holder.radioButtonInerItemNameasas.setChecked(false);
        }
        holder.radioButtonInerItemNameasas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItemPosition = position;
                if (comboSectionValueItems.getCombo_Topping_Allow().equalsIgnoreCase("Yes")) {
                    Intent intent = new Intent(context, ExtraTopingActivity.class);
                    intent.putExtra("ITEM_NAME", titleName);
                    intent.putExtra("ITEM_DISCRIPTION", titleDiscription);
                    intent.putExtra("ITEM_POSITION", itemPosition);
                    intent.putExtra("FREE_ALLOWED_VALUE", strFreeAllowedValue);
                    intent.putExtra("ItemID", comboSectionValueItems.getItemID());
                    intent.putExtra("COMBO_SLOT_ITEMID", strComboslotId);
                    intent.putExtra("FOOD_ITEM_SIZEID", comboSectionValueItems.getFoodItemSizeID());
                    context.startActivity(intent);
                }
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return comboSectionValueItemLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialRadioButton radioButtonInerItemNameasas;

        public ViewHolder(View itemView) {
            super(itemView);
            this.radioButtonInerItemNameasas = (MaterialRadioButton) itemView.findViewById(R.id.radioButtonInerItemNameasas);
        }
    }
}
