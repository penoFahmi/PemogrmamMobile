package com.peno.mierantauptk.admin;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.api.ApiClient;
import com.peno.mierantauptk.api.RecipeApiService;
import com.peno.mierantauptk.api.WeatherApiService;
import com.peno.mierantauptk.models.RecipeResponse;
import com.peno.mierantauptk.models.WeatherResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private CardView KelolaMenuCard, penjualanCard, riwayatCard;
    private CardView weatherCard, recipeCard;
    private TextView weatherName, weatherTemp, weatherLocation, recipeName, rekomendasiName;
    private ImageView weatherImage, recipeImage;
    private SearchView searchRecipe;
    private ProgressBar progressBar; // Loading indicator

    private WeatherResponse currentWeather; // Store weather data
    private RecipeResponse.Recipe currentRecipe; // Store recipe data

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        weatherCard = view.findViewById(R.id.weatherCard);
        recipeCard = view.findViewById(R.id.recipeCard);
        weatherName = view.findViewById(R.id.weatherName);
        weatherTemp = view.findViewById(R.id.weatherTemp);
        weatherLocation = view.findViewById(R.id.weatherLocation);
        weatherImage = view.findViewById(R.id.weatherImage);
        searchRecipe = view.findViewById(R.id.searchRecipe);
        recipeName = view.findViewById(R.id.recipeName);
        recipeImage = view.findViewById(R.id.recipeImage);
        rekomendasiName = view.findViewById(R.id.rekomendasiName);
        progressBar = view.findViewById(R.id.progressBar);

        KelolaMenuCard = view.findViewById(R.id.KelolaMenuCard);
        penjualanCard = view.findViewById(R.id.penjualanCard);
        riwayatCard = view.findViewById(R.id.riwayatCard);

        KelolaMenuCard.setOnClickListener(v -> replaceFragment(new MenuFragment()));
        penjualanCard.setOnClickListener(v -> replaceFragment(new KasirFragment()));
        riwayatCard.setOnClickListener(v -> replaceFragment(new RiwayatFragment()));

        // Fetch weather and recipe data
        fetchWeatherData("Pontianak");
        fetchRecipes("Bakso");

        // Restore UI state if data exists
        if (currentWeather != null) {
            updateWeatherCard(currentWeather);
        }

        if (currentRecipe != null) {
            updateRecipeCard(currentRecipe);
        }

        searchRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchRecipes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void fetchWeatherData(String city) {
        progressBar.setVisibility(View.VISIBLE); // Show loading indicator

        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather("0336cebc72cf458abd7165425251501", city);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                if (response.isSuccessful() && response.body() != null) {
                    currentWeather = response.body();
                    if (isAdded() && getView() != null) {
                        updateWeatherCard(currentWeather);
                    }
                } else {
                    if (isAdded()) {
                        showWeatherPlaceholder();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                if (isAdded()) {
                    showWeatherPlaceholder();
                }
            }
        });
    }

    private void updateWeatherCard(WeatherResponse weather) {
        if (!isAdded() || getView() == null) return;

        weatherName.setText(weather.current.condition.text);
        weatherTemp.setText(String.format("%.1f°C", weather.current.tempC));
        weatherLocation.setText(weather.location.name);

        Glide.with(this)
                .load("https:" + weather.current.condition.icon)
                .into(weatherImage);

        weatherCard.setOnClickListener(v -> openWeatherDetailFragment(weather));
    }

    private void fetchRecipes(String query) {
        progressBar.setVisibility(View.VISIBLE); // Show loading indicator

        RecipeApiService apiService = ApiClient.getEdamamClient().create(RecipeApiService.class);
        Call<RecipeResponse> call = apiService.getRecipes(query, "67b2fbc0", "d4362b19aff739ca5e1ce7e9502a5e9d");

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                if (response.isSuccessful() && response.body() != null && !response.body().hits.isEmpty()) {
                    currentRecipe = response.body().hits.get(0).recipe;
                    if (isAdded() && getView() != null) {
                        updateRecipeCard(currentRecipe);
                    }
                } else {
                    if (isAdded()) {
                        showRecipePlaceholder();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator
                if (isAdded()) {
                    showRecipePlaceholder();
                }
            }
        });
    }

    private void updateRecipeCard(RecipeResponse.Recipe recipe) {
        if (!isAdded() || getView() == null) return;

        recipeName.setText(recipe.label);
        Glide.with(this).load(recipe.image).into(recipeImage);

        recipeCard.setOnClickListener(v -> openRecipeDetail(recipe));
    }

    private void showWeatherPlaceholder() {
        weatherName.setText("No data available");
        weatherTemp.setText("--");
        weatherLocation.setText("Unknown location");

        Glide.with(this)
                .load(R.drawable.ic_profile) // Placeholder image
                .into(weatherImage);
    }

    private void showRecipePlaceholder() {
        recipeName.setText("No recipe available");

        Glide.with(this)
                .load(R.drawable.ic_profile) // Placeholder image
                .into(recipeImage);
    }

    private void openWeatherDetailFragment(WeatherResponse weather) {
        Bundle bundle = new Bundle();
        bundle.putString("temp", String.valueOf(weather.current.tempC));
        bundle.putString("description", weather.current.condition.text);

        WeatherDetailFragment fragment = new WeatherDetailFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRecipeDetail(RecipeResponse.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putString("label", recipe.label);
        bundle.putString("image", recipe.image);
        bundle.putString("url", recipe.url);
        bundle.putStringArrayList("ingredients", new ArrayList<>(recipe.ingredientLines));

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragment, fragment)
                .commit();
    }
}
