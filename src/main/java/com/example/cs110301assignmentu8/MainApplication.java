package com.example.cs110301assignmentu8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
    // Global search history that persists across the application
    private static List<WeatherHistory> globalSearchHistory = new ArrayList<>();
    
    public static List<WeatherHistory> getGlobalSearchHistory() {
        return globalSearchHistory;
    }
    
    public static void addToGlobalHistory(WeatherHistory history) {
        globalSearchHistory.add(history);
        // Keep only 10 most recent items
        if (globalSearchHistory.size() > 10) {
            globalSearchHistory.remove(0); // Remove oldest item
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("form-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        stage.setTitle("Weather Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
