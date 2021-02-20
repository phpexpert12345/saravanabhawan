package com.harperskebab.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harperskebab.databinding.LanguageListItemRowBinding;
import com.harperskebab.model.Language;
import com.harperskebab.view.adapter.viewholders.LanguageViewHolder;
import com.harperskebab.view.ui.activities.MapActivity;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageViewHolder> {
    private static final String TAG = "LanguageAdapter";

    private Context context;
    private List<Language> languages;
    private OnItemClick onItemClick;
    private String defaultLanguageCode;

    public LanguageAdapter(Context context, List<Language> languages, String defaultLanguageCode, OnItemClick onItemClick) {
        this.context = context;
        this.languages = languages;
        this.defaultLanguageCode = defaultLanguageCode;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LanguageViewHolder(LanguageListItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = languages.get(position);

        try {
            holder.getBinding().radioButtonLanguage.setText(language.getLangName());

            Glide.with(context)
                    .load(language.getLangIcon())
                    .into(holder.getBinding().imageViewLanguage);

            if (defaultLanguageCode.equals(language.getLangCode())) {
                holder.getBinding().radioButtonLanguage.setChecked(true);
            } else {
                holder.getBinding().radioButtonLanguage.setChecked(false);
            }

            holder.getBinding().radioButtonLanguage.setOnClickListener(v -> {
                defaultLanguageCode = language.getLangCode();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LANG_IMAGE", language.getLangIcon());
                editor.apply();
                notifyDataSetChanged();
                onItemClick.onClick(position, language);
            });

            holder.getBinding().getRoot().setOnClickListener(v -> {
                defaultLanguageCode = language.getLangCode();
                notifyDataSetChanged();
                onItemClick.onClick(position, language);
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Exception", e);
        }

    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public interface OnItemClick {
        void onClick(int position, Language language);
    }

}
