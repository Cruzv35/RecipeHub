package com.vebration35.myrecipeapp.subclass;

import android.app.Activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubDate {
    private Activity activity;
    private static SubDate instance;
    private SubDate(Activity activity) {
        this.activity = activity;
    }
    public static SubDate getInstance(Activity activity) {
        if (instance == null) {
            instance = new SubDate(activity);
        }
        return instance;
    }
    public java.util.Date today(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
    public String reFormat(String newFormat, java.util.Date date) {
        DateFormat dateFormat1 = new SimpleDateFormat(newFormat);
        return dateFormat1.format(date);
    }
    public String reFormat(String oldFormat,String newFormat,String date) {
        SimpleDateFormat input = new SimpleDateFormat(oldFormat);
        SimpleDateFormat output = new SimpleDateFormat(newFormat);
        try {
            java.util.Date oneWayTripDate = input.parse(date);
            return  output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "date conversion failed";
    }
}
