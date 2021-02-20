package com.harperskebab.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;
import com.harperskebab.R;
import com.harperskebab.model.Suggestion;
import com.harperskebab.view.ui.activities.AddressFromPostCodeActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class AutocompleteAdapter extends RecyclerView.Adapter<AutocompleteAdapter.ViewHolder>{
    private Context context;
    ArrayList<Suggestion> suggestions;
//    private OnLocationPoscodeClick onLocationPinClick;

    public AutocompleteAdapter(Context context, ArrayList<Suggestion> suggestions) {
        this.context = context;
        this.suggestions=suggestions;
//        this.onLocationPinClick = onLocationPinClick;
    }


    @Override
    public AutocompleteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.selectionpostcode_item, parent, false);
        AutocompleteAdapter.ViewHolder viewHolder = new AutocompleteAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AutocompleteAdapter.ViewHolder holder, int position) {
//        ComboSectionValueItems comboSectionValueItems = comboSectionValueItemLists.get(position);
        holder.txtName.setText(suggestions.get(position).getAddress());
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strId=suggestions.get(position).getId();
                String val=suggestions.get(position).getAddress();
                if (context instanceof AddressFromPostCodeActivity) {
                    ((AddressFromPostCodeActivity)context).onClick(strId,val);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtName = (MaterialTextView) itemView.findViewById(R.id.txtName);
        }
    }

}
