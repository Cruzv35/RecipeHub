package com.vebration35.myrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.vebration35.myrecipeapp.Google.Firestore.Firestore;
import com.vebration35.myrecipeapp.model.RecipeModel;
import com.vebration35.myrecipeapp.model.UserModel;
import com.vebration35.myrecipeapp.subclass.Drawable;
import com.vebration35.myrecipeapp.subclass.Navigation;
import com.vebration35.myrecipeapp.subclass.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity implements View.OnClickListener{
    String TAG = "MainActivity";
    Activity activity = this;
    SharedPref sharedPref = SharedPref.getInstance(activity);
    Navigation navigation = Navigation.getInstance(activity);
    Firestore firestore = Firestore.getInstance(activity);
    Drawable drawable = Drawable.getInstance(activity);

    List<String> ingredient = new ArrayList<>();
    List<String> instruction = new ArrayList<>();
    List<String> notes = new ArrayList<>();

    LinearLayout llContainerInstructions,llContainerIngredient,llContainerNotes;
    TextView tvPageTitle,tvCategory;
    TextView tv_username,tv_email;
    ImageView ivProfile;
    RecipeModel recipeModel;
    Gson gson;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        gson = new Gson();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        declaration();

        if (getIntent().getExtras().getString("dataModel") != null){
            recipeModel = gson.fromJson(getIntent().getExtras().getString("dataModel"), RecipeModel.class);
            ingredient = recipeModel.ingredient;
            instruction = recipeModel.instruction;
            notes = recipeModel.notes;
            loadData();
        }else{
            Toast.makeText(activity, "File is Empty", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 1000);
        }
    }
    void declaration(){
        tvPageTitle = findViewById(R.id.tvPageTitle);
        tvCategory = findViewById(R.id.tvCategory);
        ivProfile = findViewById(R.id.ivProfile);
        tv_username = findViewById(R.id.tv_username);
        tv_email = findViewById(R.id.tv_email);

        llContainerInstructions = findViewById(R.id.llContainerInstructions);
        llContainerIngredient = findViewById(R.id.llContainerIngredient);
        llContainerNotes = findViewById(R.id.llContainerNotes);
    }
    void loadData(){
        tvPageTitle.setText(recipeModel.recipeName);
        tvCategory.setText(recipeModel.category);

        tv_username.setText(recipeModel.user.displayName);
        tv_email.setText(recipeModel.user.email);
        Glide.with(getApplicationContext()).load(recipeModel.user.profileUrl).into(ivProfile);
        loadIngredient();
        loadInstruction();
        loadNotes();
    }

    boolean editIngredient = false;
    boolean editInstruction = false;
    boolean editNotes = false;
    void loadIngredient(){
        final List<String> data = ingredient;
        if(llContainerIngredient.getChildCount() > 0){
            llContainerIngredient.removeAllViews();
            Log.d("itemListContainer", "removeAllViews: ");
        }
        boolean isPrimary = false;
        if(data.size() != 0){
            for(int i = 0 ; i < data.size() ; i ++){
                final int row = i;
                String title = data.get(i);
                View item;
                if(isPrimary){
                    isPrimary = false;
                    item = getLayoutInflater().inflate(R.layout.item1, null);
                }else{
                    isPrimary = true;
                    item = getLayoutInflater().inflate(R.layout.item2, null);
                }
                TextView tvTitle = item.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                LinearLayout viewRemove = item.findViewById(R.id.viewRemove);
                if(editIngredient){
                    viewRemove.setVisibility(View.VISIBLE);
                    viewRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("loadIngredient",data.size() + " remove  " + row);
                            Log.d("loadIngredient",ingredient.size() + " remove  " + row);
                            data.remove(row);
                            ingredient = data;
                            loadIngredient();
                        }
                    });
                }else {
                    viewRemove.setVisibility(View.GONE);
                }
                llContainerIngredient.addView(item);
            }

        }
    }
    void loadInstruction(){
        final List<String> data = instruction;
        if(llContainerInstructions.getChildCount() > 0){
            llContainerInstructions.removeAllViews();
            Log.d("itemListContainer", "removeAllViews: ");
        }
        boolean isPrimary = false;
        if(data.size() != 0){
            for(int i = 0 ; i < data.size() ; i ++){
                final int row = i;
                String title = data.get(i);
                View item;
                if(isPrimary){
                    isPrimary = false;
                    item = getLayoutInflater().inflate(R.layout.item1, null);
                }else{
                    isPrimary = true;
                    item = getLayoutInflater().inflate(R.layout.item2, null);
                }
                TextView tvTitle = item.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                LinearLayout viewRemove = item.findViewById(R.id.viewRemove);
                if(editInstruction){
                    viewRemove.setVisibility(View.VISIBLE);
                }else {
                    viewRemove.setVisibility(View.GONE);
                }
                viewRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("loadInstruction",data.size() + " remove  " + row);
                        Log.d("loadInstruction",instruction.size() + " remove  " + row);
                        data.remove(row);
                        instruction = data;
                        loadInstruction();
                    }
                });
                llContainerInstructions.addView(item);
            }

        }


    }
    void loadNotes(){
        final List<String> data = notes;
        if(llContainerNotes.getChildCount() > 0){
            llContainerNotes.removeAllViews();
            Log.d("itemListContainer", "removeAllViews: ");
        }
        boolean isPrimary = false;
        if(data.size() != 0){
            for(int i = 0 ; i < data.size() ; i ++){
                final int row = i;
                String title = data.get(i);
                View item;
                if(isPrimary){
                    isPrimary = false;
                    item = getLayoutInflater().inflate(R.layout.item1, null);
                }else{
                    isPrimary = true;
                    item = getLayoutInflater().inflate(R.layout.item2, null);
                }
                TextView tvTitle = item.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                LinearLayout viewRemove = item.findViewById(R.id.viewRemove);
                if(editNotes){
                    viewRemove.setVisibility(View.VISIBLE);
                }else {
                    viewRemove.setVisibility(View.GONE);
                }
                viewRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("loadNotes",data.size() + " remove  " + row);
                        Log.d("loadNotes",instruction.size() + " remove  " + row);
                        data.remove(row);
                        notes = data;
                        loadNotes();
                    }
                });
                llContainerNotes.addView(item);
            }

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                finish();
                break;

        }
    }
}