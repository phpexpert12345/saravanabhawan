package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harperskebab.databinding.AddressListItemRowBinding;
import com.harperskebab.databinding.CardListItemRowBinding;
import com.harperskebab.model.CardList;
import com.harperskebab.model.Deliveryaddress;
import com.harperskebab.view.adapter.viewholders.AddressViewHolder;
import com.harperskebab.view.adapter.viewholders.CardListViewHolder;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListViewHolder> {
    private static final String TAG = "CardAdapter";

    private Context context;
    private List<CardList> cardLists;
    private OnCardClick onCardClick;
    private int selectedFoodItemPosition = 0;

    public CardListAdapter(Context context, List<CardList> cardLists, OnCardClick onCardClick) {
        this.context = context;
        this.cardLists = cardLists;
        this.onCardClick = onCardClick;
    }

    @NonNull
    @Override
    public CardListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardListViewHolder(CardListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardListViewHolder holder, int position) {
        CardList cardList = cardLists.get(position);

        try {
            holder.getBinding().txtCardNumber.setText(cardList.getCard_number());
            holder.getBinding().txtExpDate.setText(cardList.getCard_expire_month()+"/"+cardList.getCard_expire_year());

//            if (selectedFoodItemPosition == position) {
//                holder.getBinding().radioButtonAddressTitle.setChecked(true);
//            } else {
//                holder.getBinding().radioButtonAddressTitle.setChecked(false);
//            }

            holder.itemView.setOnClickListener(v -> {
                selectedFoodItemPosition = position;

                notifyDataSetChanged();
            });

//            holder.getBinding().checkBoxSaveCard.setOnClickListener(v -> {
//                selectedFoodItemPosition = position;
//                if (isCh){
//                    onCardClick.onClick(position, cardList);
//                }else {
//                    holder.getBinding().radioButtonCardNumber.setChecked(false);
//                }
////                onCardClick.onClick(position, cardList);
//                notifyDataSetChanged();
//            });
            holder.getBinding().checkCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub

                    selectedFoodItemPosition = position;
                    if (isChecked) {
                        onCardClick.onClick(position,isChecked, cardList);
                    } else {
                        holder.getBinding().radioButtonCardNumber.setChecked(false);
                        onCardClick.onClick(position,isChecked, cardList);
                    }
//                onCardClick.onClick(position, cardList);
                    notifyDataSetChanged();
                }
            });

            holder.getBinding().getRoot().setOnClickListener(v -> {
                selectedFoodItemPosition = position;
                notifyDataSetChanged();
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return cardLists.size();
    }

//    public Deliveryaddress getselecteddDeliveryaddresses() {
//        return cardLists.get(selectedFoodItemPosition);
//    }
//
//    public void remove(Deliveryaddress deliveryAddress) {
//        deliveryaddresses.remove(deliveryAddress);
//    }

    public interface OnCardClick {
        void onClick(int position,boolean isClicked, CardList cardList);
    }

}
