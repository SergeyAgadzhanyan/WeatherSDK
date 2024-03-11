package org.example.util;

import org.example.enums.SdkMode;
import org.example.service.WeatherService;
import org.example.service.impl.WeatherServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder для создания экземпляров WeatherService.
 */
public class SkdServiceBuilder {
    private static final Map<String, WeatherService> services = new HashMap<>();

    /**
     * Создает или возвращает(если есть экземпляр с таки токеном) экземпляр WeatherService для указанного API.
     *
     * @param api Ключ API для создания сервиса.
     * @return Экземпляр WeatherService.
     */
    public static WeatherService build(String api) {
        services.putIfAbsent(api, new WeatherServiceImpl(api));
        return services.get(api);
    }

    public static WeatherService build(SdkMode mode, String api) {
        services.putIfAbsent(api, new WeatherServiceImpl(mode, api));
        return services.get(api);
    }

    /**
     * Приватный конструктор, чтобы предотвратить создание экземпляров класса.
     */
    private SkdServiceBuilder() {
    }
}




