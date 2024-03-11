# WeatherService Application

This simple Java application demonstrates the usage of the `WeatherService` SDK to retrieve weather information for multiple cities. The application is built using the `SkdServiceBuilder` utility.

## Prerequisites

Before running the application, make sure you have:

- Obtain an API key from the weather data provider.
-  Initialize the WeatherService using the SkdServiceBuilder.

## How to Run

1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE.
3. Locate the `Main` class.
4. Replace the API key in the `SkdServiceBuilder.build` method with your actual API key.
5. Optionally, if you want to provide the SDK mode during initialization:

    ```java
    WeatherService service = SkdServiceBuilder.build(SdkMode.POLLING, "your-api-key-here");
    ```

   Or, set the SDK mode after initialization:

    ```java
    WeatherService service = SkdServiceBuilder.build("your-api-key-here");
    service.setMode(SdkMode.POLLING);
    ```

6. Run the `main` method in the `Main` class.

```java
public static void main(String[] args) {
    WeatherService service = SkdServiceBuilder.build("your-api-key-here");
    service.getWeather("Moscow");
    service.getWeather("London");
    service.getWeather("Chelsea");
}
