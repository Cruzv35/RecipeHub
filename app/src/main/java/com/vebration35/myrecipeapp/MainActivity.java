package com.vebration35.myrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.vebration35.myrecipeapp.Google.Firestore.Firestore;
import com.vebration35.myrecipeapp.model.RecipeModel;
import com.vebration35.myrecipeapp.subclass.Navigation;
import com.vebration35.myrecipeapp.subclass.SharedPref;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = "MainActivity";
    Activity activity = this;
    SharedPref sharedPref = SharedPref.getInstance(activity);
    Navigation navigation = Navigation.getInstance(activity);

    Firestore firestore = Firestore.getInstance(activity);
    FirebaseAuth mAuth;
    FirebaseUser user;

    List<RecipeModel> recipeList = new ArrayList<>();
    TextView viewNotes;
    LinearLayout viewContainer;
    Spinner spinnerRecipeType;

    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        loadData();

    }

    void loadData(){
        Log.d("MainActivity","loadData");
        viewContainer = findViewById(R.id.viewContainer);
        spinnerRecipeType = findViewById(R.id.spinnerRecipeType);
        TextView tv_username = findViewById(R.id.tv_username);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        viewNotes = findViewById(R.id.viewNotes);

        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(ivProfile);
        tv_username.setText(user.getDisplayName());
        loadRecipeList();


        spinnerRecipeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                loadRecipeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    boolean isPrimary = true;
    void setRecipeList(){
        if(viewContainer.getChildCount() > 0){
            viewContainer.removeAllViews();
            Log.d("llPhoto", "removeAllViews: ");
        }
        if(recipeList.size() != 0){
            viewNotes.setVisibility(View.GONE);
            int countViewAdd = 0;
            for(int i = 0 ; i < recipeList.size() ; i ++){
                final RecipeModel recipeModel = recipeList.get(i);
                String spinnerCurrentValue = spinnerRecipeType.getItemAtPosition(spinnerRecipeType.getSelectedItemPosition()).toString();
                if(recipeModel.category.equals(spinnerCurrentValue) || spinnerCurrentValue.equals("All Category")){
                    addViewToList(i);
                    countViewAdd += 1;
                }
            }
            if(countViewAdd == 0){
                Toast.makeText(activity, "This category is empty", Toast.LENGTH_SHORT).show();
            }

        }else {
            viewNotes.setVisibility(View.VISIBLE);
        }
    }
    void addViewToList(int i){
        final int row = i;
        final RecipeModel recipeModel = recipeList.get(i);
        View item;
        if(isPrimary){
            isPrimary = false;
            item = getLayoutInflater().inflate(R.layout.recipe1, null);
        }else{
            isPrimary = true;
            item = getLayoutInflater().inflate(R.layout.recipe2, null);

        }
        TextView tvName = item.findViewById(R.id.tvName);
        TextView tvDesc = item.findViewById(R.id.tvDesc);
        String recipeName = recipeModel.recipeName;
        String category = recipeModel.category;
        tvName.setText((recipeName == null || recipeName.equals("")) ? "-" : recipeName);
        tvDesc.setText((category == null || category.equals("")) ? "-" : category);


        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.startActivity(RecipeDetail.class,"dataModel",gson.toJson(recipeModel));
            }
        });
        viewContainer.addView(item);
    }
    void loadRecipeList(){
        firestore.snapRecipe().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    Log.d("process","success: "+queryDocumentSnapshots);
                    List<DocumentSnapshot> list1 = queryDocumentSnapshots.getDocuments();
                    RecipeModel recipeModel = new RecipeModel();
                    recipeList = new ArrayList<>();
                    for (DocumentSnapshot d : list1) {
                        recipeModel = d.toObject(RecipeModel.class);
                        recipeList.add(recipeModel);
                    }
                    setRecipeList();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("process","failed: "+e);
            }
        });
    }
    int RC;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RC = requestCode;
        if (resultCode == RESULT_OK) {
            loadData();
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewCreateNewRecipe:
                navigation.startActivityForResult(this,CreateNewRecipe.class,"","",0);
                break;
            case R.id.ivProfile:
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure to logout?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                navigation.restartApp(SplashScreen.class);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
                break;

        }
    }
}