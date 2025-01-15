package com.peno.mierantauptk.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;

import java.util.ArrayList;

public class RecipeDetailFragment extends Fragment {

    private TextView recipeLabel, recipeIngredients;
    private ImageView recipeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeLabel = view.findViewById(R.id.recipeLabel);
        recipeIngredients = view.findViewById(R.id.recipeIngredients);
        recipeImage = view.findViewById(R.id.recipeImage);

        if (getArguments() != null) {
            recipeLabel.setText(getArguments().getString("label", "No Title"));
            Glide.with(this).load(getArguments().getString("image")).into(recipeImage);

            ArrayList<String> ingredients = getArguments().getStringArrayList("ingredients");
            recipeIngredients.setText(String.join("\n", ingredients));
        }
    }
}
