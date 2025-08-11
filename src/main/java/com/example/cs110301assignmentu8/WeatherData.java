package com.example.cs110301assignmentu8;

public class WeatherData {
    public String name;
    public MainData main;
    public Weather[] weather;
    public Wind wind;
    public Sys sys;
    public long dt;      // thời gian hiện tại (epoch seconds)
    public int timezone; // offset giây so với UTC
    
    public static class MainData {
        public double temp;
        public double feels_like;
        public int humidity;
        public int pressure;
    }
    
    public static class Weather {
        public String main;
        public String description;
        public String icon;
    }
    
    public static class Wind {
        public double speed;
        public int deg;
    }
    
    public static class Sys {
        public String country;
    }
    
    // Getter methods for backward compatibility
    public String getCityName() { return name; }
    public MainData getMain() { return main; }
    public Weather[] getWeather() { return weather; }
    public Wind getWind() { return wind; }
    public Sys getSys() { return sys; }
}
