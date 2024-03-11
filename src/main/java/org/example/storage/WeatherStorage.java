package org.example.storage;

import org.example.model.WeatherData;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для хранилища данных о погоде.
 */
public interface WeatherStorage {

    /**
     * Сохранение информации о погоде.
     *
     * @param weatherData Объект с информацией о погоде.
     * @return Сохраненный объект с информацией о погоде.
     */
    WeatherData save(WeatherData weatherData);

    /**
     * Получение информации о погоде для указанного города.
     *
     * @param city Название города.
     * @return Объект с информацией о погоде (если существует).
     */
    Optional<WeatherData> getWeather(String city);

    /**
     * Получение списка всех объектов с информацией о погоде.
     *
     * @return Список объектов с информацией о погоде.
     */
    List<WeatherData> getAllWeatherData();
}




