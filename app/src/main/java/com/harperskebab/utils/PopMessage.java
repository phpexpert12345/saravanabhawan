package com.harperskebab.utils;

import android.content.Context;
import android.widget.Toast;

public class PopMessage {

    public static void makeShortToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void makeLongToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
