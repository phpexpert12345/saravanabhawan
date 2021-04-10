package com.harperskebab.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.R;
import com.harperskebab.databinding.FoodGalleryListItemRowBinding;
import com.harperskebab.model.Photo;
import com.harperskebab.view.adapter.viewholders.FoodGalleryViewHolder;

import java.util.List;

public class FoodGalleryAdapter extends RecyclerView.Adapter<FoodGalleryViewHolder> {
    private static final String TAG = "FoodGalleryAdapter";

    private Context context;
    private List<Photo> photos;
    private OnItemClick onItemClick;

    public FoodGalleryAdapter(Context context, List<Photo> photos, OnItemClick onItemClick) {
        this.context = context;
        this.photos = photos;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FoodGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodGalleryViewHolder(FoodGalleryListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodGalleryViewHolder holder, int position) {
        Photo photo = photos.get(position);

        try {

            Glide.with(context).load(photo.getFoodPhoto()).placeholder(R.drawable.app_logo).into(holder.getBinding().imageViewFood);
            holder.getBinding().getRoot().setOnClickListener(v -> {
                onItemClick.onClick(position, photo);
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public interface OnItemClick {
        void onClick(int position, Photo photo);
    }

}
