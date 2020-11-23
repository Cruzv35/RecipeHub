package com.vebration35.myrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vebration35.myrecipeapp.subclass.Navigation;

public class SplashScreen extends AppCompatActivity {

    Navigation navigation = Navigation.getInstance(this);
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

//        mAuth.signOut();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
//                    firebaseHandler.clearDelivery();
                    navigation.startActivityWithoutBack(MainActivity.class,"","");
                }else{
                    navigation.startActivityWithoutBack(Login.class,"","");
                }
            }
        }, 1500);

    }
}