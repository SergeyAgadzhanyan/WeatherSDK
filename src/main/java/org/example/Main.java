package org.example;


import org.example.enums.SdkMode;
import org.example.service.WeatherService;
import org.example.util.SkdServiceBuilder;

public class Main {
    public static void main(String[] args) {
        WeatherService service = SkdServiceBuilder.build("f1c3ff0a7f316aacf277656819c4a761");
        service.getWeather("Moscow");
        service.getWeather("London");
        service.getWeather("Chelsea");
    }
}
