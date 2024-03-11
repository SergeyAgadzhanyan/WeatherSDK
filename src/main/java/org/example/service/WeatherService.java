package org.example.service;

import org.example.enums.SdkMode;
import org.example.model.WeatherData;

/**
 * Интерфейс для сервиса получения информации о погоде.
 */
public interface WeatherService {

    /**
     * Получение информации о погоде для указанного города.
     *
     * @param city Название города.
     * @return Объект с информацией о погоде.
     */
    WeatherData getWeather(String city);

    /**
     * Установка режима работы сервиса.
     *
     * @param mode Режим работы сервиса (ON_DEMAND или POLLING).
     */
    void setMode(SdkMode mode);
}




