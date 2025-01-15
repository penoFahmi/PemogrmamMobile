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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.api.ApiClient;
import com.peno.mierantauptk.api.WeatherApiService;
import com.peno.mierantauptk.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    CardView KelolaMenuCard, penjualanCard, riwayatCard, CardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        CardView = view.findViewById(R.id.weatherCard);
        // Initialize the CardView
        KelolaMenuCard = view.findViewById(R.id.KelolaMenuCard);
        penjualanCard = view.findViewById(R.id.penjualanCard);
        riwayatCard = view.findViewById(R.id.riwayatCard);
        CardView = view.findViewById(R.id.weatherCard);

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

        CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherData("Jakarta"); // Ganti dengan nama kota
            }
        });
        return view;

    }

    private void fetchWeatherData(String city) {
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather("610c8937ce09473d9c972811251501", city);

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

        weatherName.setText(weather.current.condition.text);
        weatherTemp.setText(String.format("%.1f°C", weather.current.tempC));
        weatherLocation.setText(weather.location.name);

        // Load weather icon
        Glide.with(this)
                .load("https:" + weather.current.condition.icon)
                .into(weatherImage);

        CardView.setOnClickListener(v -> openWeatherDetailFragment(weather));
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
