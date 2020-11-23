package com.vebration35.myrecipeapp.subclass;

import android.app.Activity;

public class Core {
    private Activity activity;
    private static Core instance;
    private Core(Activity activity) {
        this.activity = activity;
        color = Color.getInstance(activity);
        drawable = Drawable.getInstance(activity);
        navigation = Navigation.getInstance(activity);
        sharedPref = sharedPref.getInstance(activity);
        subDate = SubDate.getInstance(activity);
//        animation = Animation.getInstance(activity);
//        language = Language.getInstance(activity);
//        onTouchImage = OnTouchImage.getInstance(activity);
//        qrCode = QRCode.getInstance(activity);
//        sound = Sound.getInstance(activity,false,false);
    }
    public static Core getInstance(Activity activity) {
        if (instance == null) {
            instance = new Core(activity);
        }
        return instance;
    }

//    public Animation animation;
//    public Language language;
//    public OnTouchImage onTouchImage;
//    public QRCode qrCode;
//    public Sound sound;
    public Color color;
    public Drawable drawable;
    public Navigation navigation;
    public SharedPref sharedPref;
    public SubDate subDate;
}
