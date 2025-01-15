package com.peno.mierantauptk.api;

import com.peno.mierantauptk.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("current.json")
    Call<WeatherResponse> getWeather(
            @Query("key") String apiKey,
            @Query("q") String cityName
    );
}
