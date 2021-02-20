package com.harperskebab.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.provider.Settings;

public class Device {

    @SuppressLint("HardwareIds")
    public static String getDeviceID(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
