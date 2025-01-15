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
    private TextView weatherName, weatherTemp, weatherLocation, recipeName;
    private ImageView weatherImage, recipeImage;
    private SearchView searchRecipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        // Initialize the CardView
        KelolaMenuCard = view.findViewById(R.id.KelolaMenuCard);
        penjualanCard = view.findViewById(R.id.penjualanCard);
        riwayatCard = view.findViewById(R.id.riwayatCard);

        // Set onClickListener
//        KelolaMenuCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open MenuFragment
//                Intent intent = new Intent(getActivity(), MenuFragment.class);
//                startActivity(intent);
//            }
//        });
//        penjualanCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open PenjualanFragment
//                Intent intent = new Intent(getActivity(), KasirFragment.class);
//                startActivity(intent);
//            }
//        });
//        userCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open UserFragment
//                Intent intent = new Intent(getActivity(), UsersFragment.class);
//            }
//        });

        KelolaMenuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with MenuFragment
                replaceFragment(new MenuFragment());
            }
        });

        penjualanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with KasirFragment
                replaceFragment(new KasirFragment());
            }
        });

        riwayatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with UsersFragment
                replaceFragment(new RiwayatFragment());
            }
        });


        fetchWeatherData("Pontianak");
        fetchRecipes("chicken");
        // Set up search functionality
        searchRecipe.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchRecipes(query); // Fetch recipes based on search query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // Ignore real-time text changes
            }
        });

        return view;

    }

    private void fetchWeatherData(String city) {
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather("0336cebc72cf458abd7165425251501", city);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherCard(response.body());
                } else {
                    Toast.makeText(getContext(), "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateWeatherCard(WeatherResponse weather) {
        TextView weatherName = requireView().findViewById(R.id.weatherName);
        TextView weatherTemp = requireView().findViewById(R.id.weatherTemp);
        TextView weatherLocation = requireView().findViewById(R.id.weatherLocation);
        ImageView weatherImage = requireView().findViewById(R.id.weatherImage);

        // Update card view with weather data
        weatherName.setText(weather.current.condition.text);
        weatherTemp.setText(String.format("%.1f°C", weather.current.tempC));
        weatherLocation.setText(weather.location.name);

        // Load weather icon
        Glide.with(this)
                .load("https:" + weather.current.condition.icon)
                .into(weatherImage);

        weatherCard.setOnClickListener(v -> openWeatherDetailFragment(weather));
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

    private void fetchRecipes(String query) {
        RecipeApiService apiService = ApiClient.getEdamamClient().create(RecipeApiService.class);
        Call<RecipeResponse> call = apiService.getRecipes(query, "67b2fbc0", "d4362b19aff739ca5e1ce7e9502a5e9d");

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().hits.isEmpty()) {
                    updateRecipeCard(response.body().hits.get(0).recipe); // Ambil resep pertama
                } else {
                    Toast.makeText(getContext(), "No recipes found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecipeCard(RecipeResponse.Recipe recipe) {
        TextView recipeName = requireView().findViewById(R.id.recipeName);
        ImageView recipeImage = requireView().findViewById(R.id.recipeImage);

        // Set data ke UI
        recipeName.setText(recipe.label);
        Glide.with(this).load(recipe.image).into(recipeImage);

        // Set click listener untuk membuka detail
        recipeCard.setOnClickListener(v -> openRecipeDetail(recipe));
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
        try {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_fragment, fragment);
            transaction.commit();
            Log.d("HomeFragment", "Fragment replaced successfully");
        } catch (Exception e) {
            Log.e("HomeFragment", "Error during fragment transaction: " + e.getMessage());
        }
    }


}
