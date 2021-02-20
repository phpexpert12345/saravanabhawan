package com.harperskebab.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class Validation {

    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+"
            + "(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean hasText(String str) {
        return !TextUtils.isEmpty(str);
    }

    public static boolean isValidWebAddress(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValidEmail(String str) {
        // pattern doesn't match so returning false
        return !Pattern.matches(EMAIL_REGEX, str);
    }

}
