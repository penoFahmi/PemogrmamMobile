package com.peno.mierantauptk.models;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("location")
    public Location location;

    @SerializedName("current")
    public Current current;

    public static class Location {
        @SerializedName("name")
        public String name;

        @SerializedName("region")
        public String region;

        @SerializedName("country")
        public String country;

        @SerializedName("localtime")
        public String localtime;
    }

    public static class Current {
        @SerializedName("temp_c")
        public double tempC;

        @SerializedName("temp_f")
        public double tempF;

        @SerializedName("condition")
        public Condition condition;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("wind_kph")
        public double windKph;

        @SerializedName("feelslike_c")
        public double feelslikeC;

        @SerializedName("uv")
        public double uv;
    }

    public static class Condition {
        @SerializedName("text")
        public String text;

        @SerializedName("icon")
        public String icon;
    }
}
