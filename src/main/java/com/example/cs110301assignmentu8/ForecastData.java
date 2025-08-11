package com.example.cs110301assignmentu8;

import java.util.List;

public class ForecastData {
    public List<ForecastItem> list;

    public static class ForecastItem {
        public long dt;
        public Main main;
        public List<Weather> weather;
        public String dt_txt; // Thêm trường này để ánh xạ với JSON

        public static class Main {
            public double temp;
        }
        public static class Weather {
            public String main;
            public String description;
            public String icon;
        }
    }
}
