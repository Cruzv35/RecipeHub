package com.vebration35.myrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vebration35.myrecipeapp.Google.Firestore.Firestore;
import com.vebration35.myrecipeapp.model.RecipeModel;
import com.vebration35.myrecipeapp.subclass.Drawable;
import com.vebration35.myrecipeapp.subclass.Navigation;
import com.vebration35.myrecipeapp.subclass.SharedPref;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CreateNewRecipe extends AppCompatActivity implements View.OnClickListener {

    String TAG = "CreateNewRecipe";
    Activity activity = this;
    SharedPref sharedPref = SharedPref.getInstance(activity);
    Navigation navigation = Navigation.getInstance(activity);
    Firestore firestore = Firestore.getInstance(activity);
    Drawable drawable = Drawable.getInstance(activity);

    TextView tvIngredient,tvInstruction,tvNotes;
    EditText edIngredients,edInstruction,edNotes;
    LinearLayout llContainerInstructions,llContainerIngredient,llContainerNotes,llPhoto;

    List<String> ingredient = new ArrayList<>();
    List<String> instruction = new ArrayList<>();
    List<String> notes = new ArrayList<>();
    List<String> photoID = new ArrayList<>();

    EditText edRecipeName;
    Spinner spinnerRecipeType;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_recipe);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        declaration();
    }

    void declaration(){
        spinnerRecipeType = findViewById(R.id.spinnerRecipeType);
        edRecipeName = findViewById(R.id.edRecipeName);


        edIngredients = findViewById(R.id.edIngredients);
        edInstruction = findViewById(R.id.edInstruction);
        edNotes = findViewById(R.id.edNotes);
        tvIngredient = findViewById(R.id.tvIngredient);
        tvInstruction = findViewById(R.id.tvInstruction);
        tvNotes = findViewById(R.id.tvNotes);
        llContainerInstructions = findViewById(R.id.llContainerInstructions);
        llContainerIngredient = findViewById(R.id.llContainerIngredient);
        llContainerNotes = findViewById(R.id.llContainerNotes);
        llPhoto = findViewById(R.id.llPhoto);
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
    void loadPhoto(){
        final List<String> data = photoID;
        if(llPhoto.getChildCount() > 0){
            llPhoto.removeAllViews();
            Log.d("llPhoto", "removeAllViews: ");
        }
        if(data.size() != 0){
            for(int i = 0 ; i < data.size() ; i ++){
                final int row = i;
                String title = data.get(i);
                View item;
                item = getLayoutInflater().inflate(R.layout.photo, null);
                ImageView image = item.findViewById(R.id.image);
                image.setImageBitmap(drawable.decodeImage(title));
                llPhoto.addView(item);
            }

        }


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.viewAddIngredient:
                ingredient.add(edIngredients.getText().toString());
                edIngredients.setText("");
                loadIngredient();
                break;
            case R.id.viewAddInstruction:
                instruction.add(edInstruction.getText().toString());
                edInstruction.setText("");
                loadInstruction();
                break;
            case R.id.viewAddNotes:
                notes.add(edNotes.getText().toString());
                edNotes.setText("");
                loadNotes();
                break;
            case R.id.tvEditIngredient:
                TextView tvEditIngredient = findViewById(R.id.tvEditIngredient);
                if(editIngredient){
                    tvEditIngredient.setText("Edit");
                    editIngredient = false;
                }else{
                    tvEditIngredient.setText("Done");
                    editIngredient = true;
                }
                loadIngredient();
                break;
            case R.id.tvEditInStruction:
                TextView tvEditInStruction = findViewById(R.id.tvEditInStruction);
                if(editInstruction){
                    tvEditInStruction.setText("Edit");
                    editInstruction = false;
                }else{
                    tvEditInStruction.setText("Done");
                    editInstruction = true;
                }
                loadInstruction();
                break;
            case R.id.tvEditNotes:
                TextView tvEditNotes = findViewById(R.id.tvEditNotes);
                if(editNotes){
                    tvEditNotes.setText("Edit");
                    editNotes = false;
                }else{
                    tvEditNotes.setText("Done");
                    editNotes = true;
                }
                loadNotes();
                break;
            case R.id.tvSubmit:
                boolean error = false;
                if(edRecipeName.getText().toString().isEmpty()){
                    error = true;
                    Toast.makeText(this, "Recipe Name is mandatory", Toast.LENGTH_SHORT).show();
                }else if(ingredient.size() == 0){
                    error = true;
                    Toast.makeText(this, "Ingredient is Required", Toast.LENGTH_SHORT).show();
                }else if(instruction.size() == 0){
                    error = true;
                    Toast.makeText(this, "Instruction is Required", Toast.LENGTH_SHORT).show();
                }
                if(!error){

                    RecipeModel  recipeModel = new RecipeModel();
                    recipeModel.ingredient = ingredient;
                    recipeModel.instruction = instruction;
                    recipeModel.photoID = photoID;
                    recipeModel.notes = notes;
                    recipeModel.recipeName = edRecipeName.getText().toString();
                    recipeModel.category = spinnerRecipeType.getItemAtPosition(spinnerRecipeType.getSelectedItemPosition()).toString();
                    RecipeModel.UserModel userModel = new RecipeModel.UserModel();
                    userModel.uid = user.getUid();
                    userModel.userProvider = user.getProviderId();
                    userModel.profileUrl = String.valueOf(user.getPhotoUrl());
                    userModel.displayName = user.getDisplayName();
                    userModel.email = user.getEmail();
                    recipeModel.user = userModel;
                    String id = UUID.randomUUID().toString();
                    recipeModel.uid = id;
                    firestore.addRecipe(recipeModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }

                break;
            case R.id.viewAddPhoto:
                selectImage(activity);
                break;

        }
    }

    String ConvertImage;
    int RC;
    Uri image;
    String mCameraFileName;
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    cameraIntent(2);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent(int RequestCode){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                    cameraIntentExternal(RequestCode);
                } else {
                    Toast.makeText(activity, "READ EXTERNAL STORAGE Permission is not granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "WRITE EXTERNAL STORAGE Permission is not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Camera Permission is not granted", Toast.LENGTH_SHORT).show();
        }
    }
    private static final int CONTENT_REQUEST=1337;
    private File output=null;
    boolean isInternal = false;
    private Uri imageUri;
    private void cameraIntentNoSave(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }
    private void cameraIntentExternal(int RequestCode){

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("mm-ss");
        File outFile;
        String newPicFile = df.format(date) + ".jpg";
//        String outPath = "/sdcard/" + newPicFile;
        String outPath = Environment.getExternalStorageDirectory()+"/seeqcare/" + newPicFile;
        outFile = new File(outPath);

        Log.d("urlPath",outFile.toString());
        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);


        File folder = new File(Environment.getExternalStorageDirectory()+"/seeqcare/");
//        File folder = new File("/sdcard/");
        boolean iscreated = false;
        if (!folder.exists()) {
            iscreated = folder.mkdirs();
        }
        if(iscreated){
            Log.d("directory create", "success");
        }else{
            Log.d("directory create", "failed");
        }
        startActivityForResult(intent, RequestCode);
    }
    private void cameraIntentInternal(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");
        String newPicFile = df.format(date) + ".jpg";
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        output=new File(dir, newPicFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, CONTENT_REQUEST);
    }
//    int RC;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RC = requestCode;
        if (resultCode == Activity.RESULT_OK) {
            processRespond(data,requestCode);
        }
    }
    void processRespond(Intent data,int RequestCode){
        if (RequestCode == 2) {
            if (data != null) {
                image = data.getData();
            }
            if (image == null && mCameraFileName != null) {
                image = Uri.fromFile(new File(mCameraFileName));
            }
//            File file = new File(mCameraFileName);
//            if (!file.exists()) {
//                file.mkdir();
//            }
            try{
                final InputStream imageStream = getContentResolver().openInputStream(image);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ConvertImage = encodeImage(selectedImage);
                UploadImageToServer();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }else if(RequestCode == 1){
            Uri selectedImage =  data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    ConvertImage = encodeImage(BitmapFactory.decodeFile(picturePath));
                    cursor.close();
                    UploadImageToServer();
                }
            }
        }else {
        }
    }
    public void UploadImageToServer() {
        new AlertDialog.Builder(activity)
                .setMessage("Please confirm you would like to upload this photo?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String id = UUID.randomUUID().toString();
                        firestore.addPhoto(id,ConvertImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                photoID.add(ConvertImage);
                                loadPhoto();
                            }
                        });
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
    private String encodeImage(Bitmap bm) {
        return drawable.encodeImage(bm);
    }
}