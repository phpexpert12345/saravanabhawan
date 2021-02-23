package com.harperskebab.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.duonavigationdrawer.views.DuoOptionView;
import com.harperskebab.R;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private LinkedList<String> mOptions = new LinkedList<>();
    private LinkedList<DuoOptionView> mOptionViews = new LinkedList<>();
    boolean isLogin;
    public MenuAdapter(Context context, LinkedList<String> options, boolean isLogin) {
        this.context = context;
        this.mOptions = options;
        this.isLogin=isLogin;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    public void setViewSelected(int position, boolean selected) {

        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(selected);
            } else {
                mOptionViews.get(i).setSelected(!selected);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String option = mOptions.get(position);
        Bitmap bitmap = null;
        if (isLogin) {

            int[]  myImageListTwo = new int[]{R.drawable.me, R.drawable.my_order, R.drawable.pay_later, R.drawable.my_address, R.drawable.my_review, R.drawable.my_ticket, R.drawable.loyalty, R.drawable.change_password, R.drawable.refer_friend, R.drawable.contactus, R.drawable.rate_us, R.drawable.logout};
             bitmap = BitmapFactory.decodeResource(context.getResources(), myImageListTwo[position]);
        }else {
            int[]  myImageListFirst = new int[]{R.drawable.login, R.drawable.signup, R.drawable.contactus, R.drawable.aboutus, R.drawable.terms_service, R.drawable.privacy_policy, R.drawable.rate_us};
            bitmap = BitmapFactory.decodeResource(context.getResources(), myImageListFirst[position]);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }

        // Using the DuoOptionView's default selectors

        optionView.bind(option,b,null, null);


        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);

        return optionView;
    }
}
