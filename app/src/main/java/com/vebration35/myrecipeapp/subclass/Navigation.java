package com.vebration35.myrecipeapp.subclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private Activity activity;
    private static Navigation instance;
    private Navigation(Activity activity) {
        this.activity = activity;
    }
    public static Navigation getInstance(Activity activity) {
        if (instance == null) {
            instance = new Navigation(activity);
        }
        return instance;
    }

    public void startFragment(FragmentManager fm, int viewId, Fragment fragment, List<Pair<String, String>> list, String backStack) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < list.size(); i++) {
            bundle.putString(list.get(i).first, list.get(i).second);
        }
        fragment.setArguments(bundle);
//        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewId, fragment);
        ft.addToBackStack(backStack);
        ft.commit();
    }
    public void startActivityWithoutBack( Class toClass, String key, String value) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
        activity.finish();
    }

    public void startActivityWithoutBack( Class toClass, List<Pair<String, String>> list) {
        Intent intent = new Intent(activity, toClass);
        for (int i = 0; i < list.size(); i++) {
            intent.putExtra(list.get(i).first, list.get(i).second);
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public void startActivity( Class toClass, String key, String value) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
    }

    public void startActivity( Class toClass, List<Pair<String, String>> list) {
        Intent intent = new Intent(activity, toClass);
        for (int i = 0; i < list.size(); i++) {
            intent.putExtra(list.get(i).first, list.get(i).second);
        }
        activity.startActivity(intent);
    }


    public void startActivityForResult(Activity activity, Class toClass, String key, String value, int requestCode) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtra(key, value);
        activity.startActivityForResult(intent, requestCode);
    }
    public void startActivityForResult( Class toClass, String key, String value, int requestCode) {
        Intent intent = new Intent(activity, toClass);
        intent.putExtra(key, value);
        activity.startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult( Class toClass, List<Pair<String, String>> list, int requestCode) {
        Intent intent = new Intent(activity, toClass);
        for (int i = 0; i < list.size(); i++) {
            intent.putExtra(list.get(i).first, list.get(i).second);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public void startActivityWithTransition( Class toClass, List<Pair<String, String>> list, Bundle value) {
        Intent intent = new Intent(activity, toClass);
        for (int i = 0; i < list.size(); i++) {
            intent.putExtra(list.get(i).first, list.get(i).second);
        }
        activity.startActivity(intent, value);
    }

    public void restartApp( Class toClass) {
        Intent i = new Intent(activity, toClass);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

}
