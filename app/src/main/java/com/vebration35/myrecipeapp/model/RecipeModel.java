package com.vebration35.myrecipeapp.model;

import java.util.List;

public class RecipeModel {// Wallet


    public List<String> ingredient;
    public List<String> instruction;
    public List<String> notes;
    public List<String> photoID;
    public String recipeName;
    public String category;
    public UserModel user;
    public String uid;



    public static class UserModel{
        public String uid;
        public String userProvider;
        public String profileUrl;
        public String email;
        public String displayName;
    }


}