package com.vebration35.myrecipeapp.subclass;

import android.app.Activity;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public class Color {
    private Activity activity;
    private static Color instance;
    private Color(Activity activity) {
        this.activity = activity;
    }
    public static Color getInstance(Activity activity) {
        if (instance == null) {
            instance = new Color(activity);
        }
        return instance;
    }
    public int get(@ColorRes int id){
        return ContextCompat.getColor(activity, id);
    }
}
