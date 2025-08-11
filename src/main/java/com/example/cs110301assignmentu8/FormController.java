package com.example.cs110301assignmentu8;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class FormController {
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;
    
    private WeatherService weatherService;
    
    public FormController() {
        this.weatherService = new WeatherService();
    }

    @FXML
    public void handleSubmit(ActionEvent event) throws IOException {
        String latText = latitudeField.getText().trim();
        String lonText = longitudeField.getText().trim();
        
        // Validate input data
        if (latText.isEmpty() || lonText.isEmpty()) {
            showAlert("Error", "Please enter both latitude and longitude!");
            return;
        }
        
        try {
            double lat = Double.parseDouble(latText);
            double lon = Double.parseDouble(lonText);
            
            // Check coordinate limits
            if (lat < -90 || lat > 90) {
                showAlert("Error", "Latitude must be between -90 and 90!");
                return;
            }
            if (lon < -180 || lon > 180) {
                showAlert("Error", "Longitude must be between -180 and 180!");
                return;
            }
            
            // Call weather API
            WeatherData weatherData = weatherService.getWeatherData(lat, lon);
            
            // Save to global history
            if (weatherData != null && weatherData.getMain() != null && weatherData.getWeather() != null && weatherData.getWeather().length > 0) {
                WeatherHistory history = new WeatherHistory(
                    weatherData.getCityName(),
                    lat,
                    lon,
                    weatherData.getMain().temp,
                    weatherData.getWeather()[0].description
                );
                MainApplication.addToGlobalHistory(history);
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result-view.fxml"));
            Parent root = loader.load();

            ResultController resultController = loader.getController();
            resultController.setWeatherData(weatherData, lat, lon);
            resultController.setSearchHistory(MainApplication.getGlobalSearchHistory());

            Stage stage = (Stage) latitudeField.getScene().getWindow();

            // Fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(e -> {
                Scene scene = new Scene(root, 600, 400);
                stage.setScene(scene);

                // Fade in effect
                FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for latitude and longitude!");
        } catch (IOException e) {
            e.printStackTrace(); // Thêm dòng này để xem lỗi thực tế trên console
            showAlert("Error", "Cannot connect to weather API. Please try again later!\n" + e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
