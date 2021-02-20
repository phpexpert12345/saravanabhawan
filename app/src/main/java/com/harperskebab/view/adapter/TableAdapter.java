package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.TableListItemRowBinding;
import com.harperskebab.model.Table;
import com.harperskebab.view.adapter.viewholders.TableViewHolder;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {
    private static final String TAG = "TableAdapter";

    private Context context;
    private List<Table> tables;
    private OnClick onClick;
    private Long selectTableID = -1L;

    public TableAdapter(Context context, List<Table> tables, Long selectTableID, OnClick onClick) {
        this.context = context;
        this.tables = tables;
        this.selectTableID = selectTableID;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableViewHolder(TableListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tables.get(position);

        try {
            holder.getBinding().imageViewTable.setImageResource(R.drawable.ic_table);
//            if (table.getTableIconImg().equals("")){
//                holder.getBinding().imageViewTable.setImageResource(R.drawable.ic_table);
//            }else {
//                Glide.with(context).load(table.getTableIconImg()).into(holder.getBinding().imageViewTable);
//            }
            holder.getBinding().textViewTableNo.setText(table.getTableNumber() + " " + table.getTableName());

            holder.getBinding().textViewPeopleNo.setText(table.getNumberOfPeople());

            holder.getBinding().getRoot().setOnClickListener(v -> {
                selectTableID = table.getId();
                notifyDataSetChanged();
                onClick.onClick(position, table);
            });

            if (selectTableID.equals(table.getId())) {
                holder.getBinding().viewCircle.setBackground(context.getDrawable(R.drawable.circle_background));
            } else {
                holder.getBinding().viewCircle.setBackground(null);
            }

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public interface OnClick {
        void onClick(int position, Table table);
    }

}
