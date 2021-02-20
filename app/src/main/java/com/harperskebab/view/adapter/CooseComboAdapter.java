package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.harperskebab.databinding.ChooseComboItemBinding;
import com.harperskebab.model.CardList;
import com.harperskebab.model.ComboSectionList;
import com.harperskebab.view.adapter.viewholders.ChooseComboViewHolder;
import com.harperskebab.view.ui.activities.ComboSelectionActivity;

import java.util.List;

public class CooseComboAdapter extends RecyclerView.Adapter<ChooseComboViewHolder> {
    private static final String TAG = "CardAdapter";

    private Context context;
    private int selectedFoodItemPosition = -1;
    List<ComboSectionList> comboSectionLists;
    private CooseComboInerItemsAdapter cooseComboInerItemsAdapter;
    String titleName, titleDiscription;
int itemPosition;
boolean isSelected=false;
    public CooseComboAdapter(Context context, String titleName, String titleDiscription,int itemPosition, List<ComboSectionList> comboSectionLists) {
        this.context = context;
        this.comboSectionLists = comboSectionLists;
        this.titleName = titleName;
        this.titleDiscription = titleDiscription;
        this.itemPosition=itemPosition;
    }

    @NonNull
    @Override
    public ChooseComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChooseComboViewHolder(ChooseComboItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ChooseComboViewHolder holder, int position) {
        ComboSectionList comboSectionList = comboSectionLists.get(position);

        try {
            holder.getBinding().txtName.setText(comboSectionList.getComboSectionValue().get(0).getSlot_Option_Name());

            if (selectedFoodItemPosition == position && isSelected) {
                String strFreeAllowedValue = comboSectionList.getComboSectionValue().get(0).getFree_allowed();
                String strComboslotId = comboSectionList.getComboSectionValue().get(0).getComboslot_id();
                holder.getBinding().recComboItemm.setLayoutManager(new LinearLayoutManager(context));
                cooseComboInerItemsAdapter = new CooseComboInerItemsAdapter(context, strComboslotId, titleName, titleDiscription, itemPosition, strFreeAllowedValue, comboSectionList.getComboSectionValue().get(0).getComboSectionValueItems());
                holder.getBinding().recComboItemm.setAdapter(cooseComboInerItemsAdapter);
                holder.getBinding().txtExpDate.setText("-");
                holder.getBinding().recComboItemm.setVisibility(View.VISIBLE);
            }else if (selectedFoodItemPosition == position && !isSelected){
//                holder.getBinding().txtExpDate.setText("+");
                String strFreeAllowedValue = comboSectionList.getComboSectionValue().get(0).getFree_allowed();
                String strComboslotId = comboSectionList.getComboSectionValue().get(0).getComboslot_id();
                holder.getBinding().recComboItemm.setLayoutManager(new LinearLayoutManager(context));
                cooseComboInerItemsAdapter = new CooseComboInerItemsAdapter(context, strComboslotId, titleName, titleDiscription, itemPosition, strFreeAllowedValue, comboSectionList.getComboSectionValue().get(0).getComboSectionValueItems());
                holder.getBinding().recComboItemm.setAdapter(cooseComboInerItemsAdapter);
                holder.getBinding().txtExpDate.setText("-");
                holder.getBinding().recComboItemm.setVisibility(View.VISIBLE);
            } else {
                holder.getBinding().txtExpDate.setText("+");
                holder.getBinding().recComboItemm.setVisibility(View.GONE);
            }

//            holder.getBinding().txtExpDate.setText(cardList.getCard_expire_month()+"/"+cardList.getCard_expire_year());

//            if (selectedFoodItemPosition == position) {
//                holder.getBinding().radioButtonAddressTitle.setChecked(true);
//            } else {
//                holder.getBinding().radioButtonAddressTitle.setChecked(false);
//            }

            holder.getBinding().laySelectItem.setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                if (isSelected){
                    isSelected=false;
                }else {
                   isSelected=true;
                }
                notifyDataSetChanged();
            });


        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return comboSectionLists.size();
    }

    public interface OnCardClick {
        void onClick(int position, boolean isClicked, CardList cardList);
    }

}
