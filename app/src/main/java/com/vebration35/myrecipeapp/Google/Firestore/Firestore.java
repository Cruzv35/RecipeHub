package com.vebration35.myrecipeapp.Google.Firestore;


import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.vebration35.myrecipeapp.model.RecipeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Firestore {
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseFirestoreSettings settings;
    private static Firestore instance;
    private Activity activity;

    private Firestore(Activity activity) {
        this.activity = activity;
    }
    public static Firestore getInstance(Activity activity) {
        if (instance == null) {
            instance = new Firestore(activity);
            FirebaseApp.initializeApp(activity);
            firebaseFirestore = FirebaseFirestore.getInstance();
            settings = new FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build();
            firebaseFirestore.setFirestoreSettings(settings);
        }
        return instance;
    }
    public Task<QuerySnapshot> snapAuth(){
        return firebaseFirestore.collection("auth").get();
    }

    public Task<DocumentSnapshot> snapAuth(String userID){
        return firebaseFirestore.collection("auth").document(userID).get();
    }

    public Task<Void> register(String uid,String providerID, String username, String displayName, String email, String ProfileUrl) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("providerID", providerID);
        userMap.put("username", username);
        userMap.put("displayName", displayName);
        userMap.put("ProfileUrl", ProfileUrl);
        return firebaseFirestore.collection("auth").document(uid).set(userMap);
    }
    public Task<Void> addRecipe(RecipeModel recipeModel) {
        return firebaseFirestore.collection("recipe").document(recipeModel.uid).set(recipeModel);
    }
    public Task<Void> addPhoto(String photoID,String data) {
        Map<String, String> photoMap = new HashMap<>();
        photoMap.put("photoID", data);
        photoMap.put("data", data);
        return firebaseFirestore.collection("photo").document(photoID).set(photoMap);
    }

    public Task<QuerySnapshot> snapRecipe(){
        return firebaseFirestore.collection("recipe").get();
    }

}