package com.vebration35.myrecipeapp.subclass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPref {
    private Activity activity;
    private static SharedPref instance;
    private SharedPref(Activity activity) {
        this.activity = activity;
    }
    public static SharedPref getInstance(Activity activity) {
        if (instance == null) {
            instance = new SharedPref(activity);
        }
        return instance;
    }

    public void saveString(String key,String value) {
        SharedPreferences preferences = activity.getSharedPreferences("pref_save_string", Context.MODE_PRIVATE);
        if(key.contains("permanent")){
            preferences = activity.getSharedPreferences("pref_save_string_permanent", Context.MODE_PRIVATE);
        }
        Log.d("saveString", key+": "+value);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public void saveInt(String key,int value) {
        SharedPreferences preferences = activity.getSharedPreferences("pref_save_int", Context.MODE_PRIVATE);
        if(key.equals("permanent")){
            preferences = activity.getSharedPreferences("pref_save_int_permanent", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public void saveBool(String key,boolean value) {
        SharedPreferences preferences = activity.getSharedPreferences("pref_save_bool", Context.MODE_PRIVATE);
        if(key.equals("permanent")){
            preferences = activity.getSharedPreferences("pref_save_bool_permanent", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public String loadString(String key){
        SharedPreferences preferences = activity.getSharedPreferences("pref_save_string", Context.MODE_PRIVATE);
        if(key.equals("permanent")){
            preferences = activity.getSharedPreferences("pref_save_string_permanent", Context.MODE_PRIVATE);
        }
        Log.d("loadString", key+": "+preferences.getString(key, ""));
        return preferences.getString(key, "");
    }
    public Integer loadInt(String key){
        SharedPreferences preferences = activity.getSharedPreferences("pref_save_int", Context.MODE_PRIVATE);
        if(key.equals("permanent")){
            preferences = activity.getSharedPreferences("pref_save_int_permanent", Context.MODE_PRIVATE);
        }
        Log.d(key,""+preferences.getInt(key, 0));
        return preferences.getInt(key, 0);
    }
    public boolean loadBool(String key){
        SharedPreferences preferences = activity.getSharedPreferences("pref_save_bool", Context.MODE_PRIVATE);
        if(key.equals("permanent")){
            preferences = activity.getSharedPreferences("pref_save_bool_permanent", Context.MODE_PRIVATE);
        }
        Log.d(key,""+preferences.getBoolean(key, false));
        return preferences.getBoolean(key, false);
    }
}
