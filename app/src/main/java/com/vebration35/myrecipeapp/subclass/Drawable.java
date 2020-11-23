package com.vebration35.myrecipeapp.subclass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import androidx.core.content.ContextCompat;

public class Drawable {

    private Activity activity;
    private static Drawable instance;
    private Drawable(Activity activity) {
        this.activity = activity;
    }
    public static Drawable getInstance(Activity activity) {
        if (instance == null) {
            instance = new Drawable(activity);
        }
        return instance;
    }

    public android.graphics.drawable.Drawable get(int resID){
        return ContextCompat.getDrawable(activity, resID);
    }
    public String getResName(int resID){
        return activity.getResources().getResourceName(resID);
    }
    public String getResEntryName(int resID){
        return activity.getResources().getResourceEntryName(resID);
    }
    public String encodeImage(Bitmap bm) {
        if(bm != null){
            bm = Bitmap.createScaledBitmap(bm,100,100,false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encImage;
        }else{
            Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show();
            return  "";
        }
    }
    public Bitmap decodeImage(String base64) {
        byte[] imageAsBytes = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
