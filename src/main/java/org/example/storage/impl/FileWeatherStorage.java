package org.example.storage.impl;

import org.example.model.WeatherData;
import org.example.storage.WeatherStorage;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса хранилища данных о погоде с использованием Hibernate.
 */
public class FileWeatherStorage implements WeatherStorage {

    /**
     * Сохранение информации о погоде.
     *
     * @param weatherData Объект с информацией о погоде.
     * @return Сохраненный объект с информацией о погоде.
     */
    @Override
    public WeatherData save(WeatherData weatherData) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<WeatherData> allWeatherData = getAllWeatherData();
            Long id = allWeatherData.stream()
                    .filter(w -> w.getName().equals(weatherData.getName()))
                    .map(WeatherData::getId).findFirst().orElse(null);
            weatherData.setId(id);
            if (id != null || allWeatherData.size() < 10) session.saveOrUpdate(weatherData);
            return weatherData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение информации о погоде для указанного города.
     *
     * @param city Название города.
     * @return Объект с информацией о погоде (если существует).
     */
    @Override
    public Optional<WeatherData> getWeather(String city) {
        WeatherData weatherData = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            weatherData = session.createQuery("From WeatherData as w where w.name = :city ",
                            WeatherData.class)
                    .setParameter("city", city)
                    .list()
                    .stream()
                    .filter(w -> System.currentTimeMillis() / 1000 - w.getDatetime() < 10 * 60)
                    .findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(weatherData);
    }

    /**
     * Получение списка всех объектов с информацией о погоде.
     *
     * @return Список объектов с информацией о погоде.
     */
    @Override
    public List<WeatherData> getAllWeatherData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("From WeatherData", WeatherData.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
