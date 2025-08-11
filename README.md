# Weather Application

JavaFX application to view weather information based on geographic coordinates (latitude and longitude).

## Features

- **Geographic coordinate input** (latitude and longitude)
- **Detailed weather information display**:
  - Current temperature
  - Feels like temperature
  - Humidity
  - Air pressure
  - Wind information
  - Weather description
- **Dynamic weather backgrounds**: Background changes based on weather conditions (sunny, rainy, cloudy, etc.)
- **Temperature unit conversion**: Switch between Celsius and Fahrenheit
- **Search history**: Save and display 10 most recent searches during program execution
- **Beautiful interface** with smooth transition effects
- **Error handling** and user notifications

## How to use

1. Run the application with command:
   ```bash
   mvn clean javafx:run
   ```

2. Enter geographic coordinates:
   - **Latitude**: From -90 to 90 (example: 21.139 for Hanoi)
   - **Longitude**: From -180 to 180 (example: 105.502 for Hanoi)

3. Click "View Weather" button to get information

4. View results with weather-based background changes
5. Use "Switch to °F" button to convert temperature units
6. Click "History" to view previous searches
7. Click "Back" to enter new coordinates

## Example coordinates

- **Hanoi**: 21.139, 105.502
- **Ho Chi Minh City**: 10.823, 106.629
- **Da Nang**: 16.054, 108.202
- **Can Tho**: 10.045, 105.747

## Technologies used

- **JavaFX**: User interface
- **OkHttp**: HTTP client for API calls
- **Gson**: JSON response processing
- **OpenWeatherMap API**: Weather data

## API Key

The application uses OpenWeatherMap API key. The API key is embedded in the code for demo purposes. In production, the API key should be stored in a separate configuration file.

## Project structure

```
src/main/java/com/example/cs110301assignmentu8/
├── HelloApplication.java          # Main application
├── FormController.java           # Controller for input form
├── ResultController.java         # Controller for result screen
├── WeatherService.java          # Service for weather API calls
└── WeatherData.java             # Weather data model

src/main/resources/com/example/cs110301assignmentu8/
├── form-view.fxml               # Input coordinate interface
└── result-view.fxml             # Result display interface
```

## System requirements

- Java 17 or higher
- Maven 3.6+
- Internet connection for weather API calls
