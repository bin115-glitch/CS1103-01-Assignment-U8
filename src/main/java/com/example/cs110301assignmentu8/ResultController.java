package com.example.cs110301assignmentu8;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.Instant;

public class ResultController {
    @FXML
    private VBox mainContainer;
    @FXML
    private Label cityLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label weatherLabel;
    @FXML
    private Label feelsLikeLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label pressureLabel;
    @FXML
    private Label windLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button unitToggleButton;
    @FXML
    private ListView<String> forecastListView;
    @FXML
    private ImageView weatherIconView;
    
    private boolean isCelsius = true; // Mặc định hiển thị Celsius
    private List<WeatherHistory> searchHistory;
    private WeatherService weatherService = new WeatherService();

    private double cloudY = 50;
    private double cloudSpeed = 0.8;
    private Canvas cloudCanvas;
    private AnimationTimer cloudTimer;
    private double cloudX = 0;

    private WeatherData currentWeatherData; // Thêm biến này

    @FXML
    public void initialize() {
        setupCloudEffect();
        // Đổi nền khi chọn một dòng dự báo
        forecastListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0 && currentForecastData != null) {
                ForecastData.ForecastItem item = getForecastItemByIndex(newVal.intValue());
                if (item != null) {
                    String weatherMain = item.weather.get(0).main;
                    String weatherIcon = item.weather.get(0).icon;
                    String dtTxt = item.dt_txt;
                    setForecastBackground(weatherMain, weatherIcon, dtTxt);
                }
            }
        });
    }

    // Lưu lại dữ liệu forecast để dùng khi đổi nền
    private ForecastData currentForecastData;

    private ForecastData.ForecastItem getForecastItemByIndex(int index) {
        if (currentForecastData == null || currentForecastData.list == null) return null;
        int count = 0;
        for (ForecastData.ForecastItem item : currentForecastData.list) {
            if (item.dt_txt != null && item.dt_txt.endsWith("12:00:00")) {
                if (count == index) return item;
                count++;
            }
        }
        return null;
    }

    private void setupCloudEffect() {
        if (mainContainer == null) return;
        if (cloudCanvas == null) {
            cloudCanvas = new Canvas(500, 200); // Kích thước canvas, có thể điều chỉnh
            mainContainer.getChildren().add(0, cloudCanvas); // Thêm canvas lên đầu VBox
        }
        cloudTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawCloud();
                cloudX += cloudSpeed;
                if (cloudX > cloudCanvas.getWidth() + 60) { // 60 là chiều rộng đám mây
                    cloudX = -60;
                }
            }
        };
        cloudTimer.start();
    }

    private void drawCloud() {
        if (cloudCanvas == null) return;
        GraphicsContext gc = cloudCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, cloudCanvas.getWidth(), cloudCanvas.getHeight());
        // Vẽ đám mây đơn giản bằng các hình oval
        gc.setFill(Color.rgb(255,255,255,0.8));
        gc.fillOval(cloudX, cloudY, 60, 30);
        gc.fillOval(cloudX + 20, cloudY - 10, 40, 30);
        gc.fillOval(cloudX + 35, cloudY + 5, 35, 25);
    }

    public void setWeatherData(WeatherData weatherData, double lat, double lon) {
        if (weatherData == null) {
            cityLabel.setText("Cannot get weather data");
            return;
        }
        this.currentWeatherData = weatherData; // Lưu lại để dùng cho nền
        
        // Display city name and country
        String cityInfo = weatherData.getCityName();
        if (weatherData.getSys() != null && weatherData.getSys().country != null) {
            cityInfo += ", " + weatherData.getSys().country;
        }
        cityLabel.setText(cityInfo);
        
        // Display temperature
        if (weatherData.getMain() != null) {
            updateTemperatureDisplay(weatherData.getMain().temp, weatherData.getMain().feels_like);
            humidityLabel.setText(String.format("Humidity: %d%%", weatherData.getMain().humidity));
            pressureLabel.setText(String.format("Pressure: %d hPa", weatherData.getMain().pressure));
        }
        
        // Display weather information
        if (weatherData.getWeather() != null && weatherData.getWeather().length > 0) {
            WeatherData.Weather weather = weatherData.getWeather()[0];
            weatherLabel.setText(String.format("%s - %s", weather.main, weather.description));
            // Hiển thị icon trạng thái thời tiết
            setWeatherIcon(weather.icon);
            setWeatherBackground(weather.main, weather.icon, weatherData); // truyền weatherData
        }
        
        // Display wind information
        if (weatherData.getWind() != null) {
            windLabel.setText(String.format("Wind: %.1f m/s, direction %d°", 
                weatherData.getWind().speed, weatherData.getWind().deg));
        }
        
        showForecast(lat, lon);
        // Có thể điều chỉnh hiệu ứng theo thời tiết ở đây nếu muốn
        // Ví dụ: chỉ hiện đám mây khi trời nhiều mây
        // setupCloudEffect(); // Đã gọi trong initialize()
    }
    
    public void setSearchHistory(List<WeatherHistory> searchHistory) {
        this.searchHistory = searchHistory;
    }
    
    private void updateTemperatureDisplay(double tempCelsius, double feelsLikeCelsius) {
        if (isCelsius) {
            temperatureLabel.setText(String.format("%.1f°C", tempCelsius));
            feelsLikeLabel.setText(String.format("Feels like: %.1f°C", feelsLikeCelsius));
        } else {
            double tempFahrenheit = (tempCelsius * 9/5) + 32;
            double feelsLikeFahrenheit = (feelsLikeCelsius * 9/5) + 32;
            temperatureLabel.setText(String.format("%.1f°F", tempFahrenheit));
            feelsLikeLabel.setText(String.format("Feels like: %.1f°F", feelsLikeFahrenheit));
        }
    }
    
    @FXML
    public void handleUnitToggle() {
        isCelsius = !isCelsius;
        if (unitToggleButton != null) {
            unitToggleButton.setText(isCelsius ? "Switch to °F" : "Switch to °C");
        }
        // Update temperature display
        if (temperatureLabel.getText().contains("°")) {
            // Get current temperature value and convert
            String tempText = temperatureLabel.getText().replace("°C", "").replace("°F", "").trim();
            try {
                double currentTemp = Double.parseDouble(tempText);
                if (isCelsius) {
                    // Convert from Fahrenheit to Celsius
                    double tempCelsius = (currentTemp - 32) * 5/9;
                    double feelsLikeCelsius = tempCelsius - 2; // Estimate
                    updateTemperatureDisplay(tempCelsius, feelsLikeCelsius);
                } else {
                    // Convert from Celsius to Fahrenheit
                    double tempFahrenheit = (currentTemp * 9/5) + 32;
                    double feelsLikeFahrenheit = tempFahrenheit + 2; // Estimate
                    updateTemperatureDisplay(currentTemp, currentTemp - 2);
                }
            } catch (NumberFormatException e) {
                // If cannot parse, keep unchanged
            }
        }
    }
    
    // Sửa lại hàm setWeatherBackground để nhận thêm weatherData
    private void setWeatherBackground(String weatherMain, String weatherIcon, WeatherData weatherData) {
        String backgroundStyle = "";
        int hour = LocalTime.now().getHour();

        // Lấy giờ theo timezone thành phố nếu có
        if (weatherData != null && weatherData.dt != 0 && weatherData.timezone != 0) {
            ZoneId zoneId = ZoneId.ofOffset("UTC", java.time.ZoneOffset.ofTotalSeconds(weatherData.timezone));
            hour = Instant.ofEpochSecond(weatherData.dt).atZone(zoneId).getHour();
        }

        // Kết hợp trạng thái thời tiết và khung giờ
        if (hour >= 6 && hour < 9) {
            // Sáng sớm, trời hơi âm
            backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #b3c6e7, #e0eafc);";
        } else if (hour >= 9 && hour < 18) {
            // Ban ngày, trời sáng
            if ("rain".equalsIgnoreCase(weatherMain) || "drizzle".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #a1c4fd, #c2e9fb);";
            } else if ("clouds".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #B0C4DE, #778899);";
            } else {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #87CEEB, #FFD700);";
            }
        } else {
            // Ban đêm, trời tối
            if ("rain".equalsIgnoreCase(weatherMain) || "drizzle".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #232946, #3a506b);";
            } else if ("clouds".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #2F4F4F, #696969);";
            } else {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #191970, #232946);";
            }
        }

        // Apply background to main container
        if (mainContainer != null) {
            mainContainer.setStyle(backgroundStyle);
        }
    }
    
    @FXML
    public void handleShowHistory() {
        if (searchHistory == null || searchHistory.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Search History");
            alert.setHeaderText(null);
            alert.setContentText("No search history available.");
            alert.showAndWait();
            return;
        }
        
        StringBuilder historyText = new StringBuilder("Search History (10 most recent):\n\n");
        // Display from newest to oldest
        for (int i = searchHistory.size() - 1; i >= 0; i--) {
            WeatherHistory history = searchHistory.get(i);
            historyText.append(String.format("%d. %s\n", searchHistory.size() - i, history.toString()));
        }
        
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Search History");
        alert.setHeaderText(null);
        alert.setContentText(historyText.toString());
        alert.showAndWait();
    }
    
    @FXML
    public void handleBack() {
        // Go back to input screen
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("form-view.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 400, 300);
            javafx.stage.Stage stage = (javafx.stage.Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showForecast(double lat, double lon) {
        try {
            ForecastData forecastData = weatherService.getForecastData(lat, lon);
            showDailyForecast(forecastData);
        } catch (Exception e) {
            // Có thể log lỗi hoặc hiển thị thông báo
        }
    }

    private void showDailyForecast(ForecastData forecastData) {
        this.currentForecastData = forecastData;
        if (forecastData == null || forecastData.list == null || forecastListView == null) return;
        forecastListView.getItems().clear();
        for (ForecastData.ForecastItem item : forecastData.list) {
            if (item.dt_txt != null && item.dt_txt.endsWith("12:00:00")) {
                String date = item.dt_txt.substring(0, 10);
                String desc = item.weather.get(0).description;
                String temp = String.format("%.1f°C", item.main.temp);
                forecastListView.getItems().add(date + ": " + desc + ", " + temp);
            }
        }
    }

    // Đổi nền theo dt_txt (giờ của từng mốc dự báo)
    private void setForecastBackground(String weatherMain, String weatherIcon, String dtTxt) {
        String backgroundStyle = "";
        int hour = 12; // mặc định 12h nếu không parse được
        try {
            if (dtTxt != null && dtTxt.length() >= 13) {
                hour = Integer.parseInt(dtTxt.substring(11, 13));
            }
        } catch (Exception ignore) {}

        if (hour >= 6 && hour < 9) {
            backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #b3c6e7, #e0eafc);";
        } else if (hour >= 9 && hour < 18) {
            if ("rain".equalsIgnoreCase(weatherMain) || "drizzle".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #a1c4fd, #c2e9fb);";
            } else if ("clouds".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #B0C4DE, #778899);";
            } else {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #87CEEB, #FFD700);";
            }
        } else {
            if ("rain".equalsIgnoreCase(weatherMain) || "drizzle".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #232946, #3a506b);";
            } else if ("clouds".equalsIgnoreCase(weatherMain)) {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #2F4F4F, #696969);";
            } else {
                backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #191970, #232946);";
            }
        }
        if (mainContainer != null) {
            mainContainer.setStyle(backgroundStyle);
        }
    }

    private void setWeatherIcon(String iconCode) {
        if (weatherIconView != null && iconCode != null && !iconCode.isEmpty()) {
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
            weatherIconView.setImage(new Image(iconUrl, true));
        }
    }
}
