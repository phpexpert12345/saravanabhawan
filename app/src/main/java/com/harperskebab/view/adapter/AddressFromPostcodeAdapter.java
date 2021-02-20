package com.harperskebab.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.harperskebab.R;
import com.harperskebab.model.ComboSectionValueItems;
import com.harperskebab.model.RestaurantBranch;
import com.harperskebab.view.ui.activities.AddressFromPostCodeActivity;
import com.harperskebab.view.ui.activities.ExtraTopingActivity;
import com.harperskebab.view.ui.activities.MapActivity;

import java.util.ArrayList;
import java.util.List;

public class AddressFromPostcodeAdapter extends RecyclerView.Adapter<AddressFromPostcodeAdapter.ViewHolder>{
    private Context context;
    ArrayList<String> arrTestModel;
//    private OnLocationPoscodeClick onLocationPinClick;

    public AddressFromPostcodeAdapter(Context context, ArrayList<String> arrTestModel) {
        this.context = context;
        this.arrTestModel=arrTestModel;
//        this.onLocationPinClick = onLocationPinClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.selectionpostcode_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        ComboSectionValueItems comboSectionValueItems = comboSectionValueItemLists.get(position);
        holder.radioButtonAddrs.setText(arrTestModel.get(position));

    }


    @Override
    public int getItemCount() {
        return arrTestModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialRadioButton radioButtonAddrs;

        public ViewHolder(View itemView) {
            super(itemView);
            this.radioButtonAddrs = (MaterialRadioButton) itemView.findViewById(R.id.radioButtonAddrs);
        }
    }

//    public interface OnLocationPoscodeClick {
//         void onPoscodeClick(int position, String s);
//
//
//    }
}
