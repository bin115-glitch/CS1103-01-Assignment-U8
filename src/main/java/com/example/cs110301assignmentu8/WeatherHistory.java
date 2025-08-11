package com.example.cs110301assignmentu8;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherHistory {
    private String cityName;
    private double latitude;
    private double longitude;
    private double temperature;
    private String weatherDescription;
    private LocalDateTime searchTime;
    
    public WeatherHistory(String cityName, double latitude, double longitude, 
                         double temperature, String weatherDescription) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.searchTime = LocalDateTime.now();
    }
    
    // Getters
    public String getCityName() { return cityName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getTemperature() { return temperature; }
    public String getWeatherDescription() { return weatherDescription; }
    public LocalDateTime getSearchTime() { return searchTime; }
    
    public String getFormattedTime() {
        return searchTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    @Override
    public String toString() {
        return String.format("%s - %.1f°C - %s (%s)", 
            cityName, temperature, weatherDescription, getFormattedTime());
    }
}
