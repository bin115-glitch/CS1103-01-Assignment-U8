package com.example.cs110301assignmentu8;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class WeatherService {
    private static final String API_KEY = "c5e0c94496c87b7db96f54558cc3220c";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private final OkHttpClient client;
    private final Gson gson;
    
    public WeatherService() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }
    
    public WeatherData getWeatherData(double latitude, double longitude) throws IOException {
        String url = String.format("%s?lat=%.3f&lon=%.3f&appid=%s&units=metric&lang=en",
                BASE_URL, latitude, longitude, API_KEY);
        
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            
            String jsonResponse = response.body().string();
            return gson.fromJson(jsonResponse, WeatherData.class);
        }
    }

    public ForecastData getForecastData(double latitude, double longitude) throws IOException {
        String url = String.format("%s?lat=%.3f&lon=%.3f&appid=%s&units=metric&lang=en",
                FORECAST_URL, latitude, longitude, API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            String jsonResponse = response.body().string();
            return gson.fromJson(jsonResponse, ForecastData.class);
        }
    }
}
