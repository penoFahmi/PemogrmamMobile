package com.peno.mierantauptk.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeResponse {
    @SerializedName("hits")
    public List<Hit> hits;

    public static class Hit {
        @SerializedName("recipe")
        public Recipe recipe;
    }

    public static class Recipe {
        @SerializedName("label")
        public String label;

        @SerializedName("image")
        public String image;

        @SerializedName("url")
        public String url;

        @SerializedName("ingredientLines")
        public List<String> ingredientLines;
    }
}

