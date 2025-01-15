package com.peno.mierantauptk.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    private static final String BASE_URL_EDAMAM = "https://api.edamam.com/";
    private static Retrofit retrofit;
    private static Retrofit edamamRetrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getEdamamClient() {
        if (edamamRetrofit == null) {
            edamamRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_EDAMAM)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return edamamRetrofit;
    }
}
