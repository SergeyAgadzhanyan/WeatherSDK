package org.example.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.Data;
import org.example.enums.SdkMode;
import org.example.model.Geo;
import org.example.model.Weather;
import org.example.model.WeatherData;
import org.example.service.WeatherService;
import org.example.storage.WeatherStorage;
import org.example.storage.impl.FileWeatherStorage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static org.example.logger.MyLogger.log;

/**
 * Основной класс сервиса для получения информации о погоде.
 */
@Data
public class WeatherServiceImpl implements WeatherService {
    private static final String GEO_API = "http://api.openweathermap.org/geo/1.0/direct?";
    private static final String DATA_API = "https://api.openweathermap.org/data/2.5/weather?";
    private String token;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WeatherStorage storage = new FileWeatherStorage();
    private final OkHttpClient client = new OkHttpClient();
    private SdkMode mode = SdkMode.ON_DEMAND;
    private final long updateTime = 1000 * 60 * 10;
    private Timer pollingTimer;

    private final TimerTask pollingTask = new TimerTask() {
        @Override
        public void run() {
            log.info("Начало выполнения задачи опроса");
            updateWeatherData();
        }
    };

    /**
     * Конструктор с токеном.
     *
     * @param token Токен для доступа к API погоды.
     */
    public WeatherServiceImpl(String token) {
        this.token = token;
    }

    /**
     * Конструктор с установкой режима по умолчанию.
     *
     * @param mode  Режим работы сервиса.
     * @param token Токен для доступа к API погоды.
     */
    public WeatherServiceImpl(SdkMode mode, String token) {
        this.token = token;
        this.mode = mode;
    }

    /**
     * Получение информации о погоде для указанного города.
     *
     * @param city Название города.
     * @return Информация о погоде для указанного города.
     */
    @Override
    public WeatherData getWeather(String city) {
        Optional<WeatherData> weatherFromDb = storage.getWeather(city);
        return weatherFromDb.orElseGet(() -> storage.save(getWeatherFromApi(city)));
    }

    /**
     * Приватный метод для получения информации о погоде из API.
     */
    private WeatherData getWeatherFromApi(String city) {
        try {
            Geo cityGeo = getGeoFromCity(city, token);
            return getWeatherDataFromGeo(cityGeo, token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Установка режима работы сервиса.
     *
     * @param mode Режим работы сервиса.
     */
    @Override
    public void setMode(SdkMode mode) {
        if (mode == SdkMode.ON_DEMAND) startOnDemandMode();
        else startPollingMode();
        this.mode = mode;
        log.info("Начало работы в режиме: {}", mode.name());
    }

    /**
     * Приватный метод для запуска режима polling.
     */
    private void startPollingMode() {
        log.info("Начало работы в режиме опроса");
        pollingTimer = new Timer("Polling Timer");
        pollingTimer.scheduleAtFixedRate(pollingTask, updateTime, updateTime);
    }

    /**
     * Приватный метод для запуска режима on-demand.
     */
    private void startOnDemandMode() {
        pollingTimer.cancel();
        log.info("Таймер остановлен");
    }

    /**
     * Приватный метод для получения информации о погоде по координатам.
     *
     * @param cityGeo Координаты города.
     * @param token   Токен для доступа к API погоды.
     * @return Информация о погоде для указанного города.
     * @throws IOException Возникает в случае ошибки при взаимодействии с API.
     */
    private WeatherData getWeatherDataFromGeo(Geo cityGeo, String token) throws IOException {
        StringBuilder dataUrl =
                new StringBuilder()
                        .append(WeatherServiceImpl.DATA_API)
                        .append("lat=")
                        .append(cityGeo.getLat())
                        .append("&lon=")
                        .append(cityGeo.getLon())
                        .append("&appid=").append(token);
        Request request2 = new Request.Builder().url(dataUrl.toString()).build();

        Response response2 = client.newCall(request2).execute();
        String body = response2.body().string();
        JsonNode jsonNode = objectMapper.readTree(body);
        WeatherData weatherData = objectMapper.treeToValue(jsonNode, WeatherData.class);
        weatherData.setWeather(getWeatherFromJson(jsonNode));
        return weatherData;
    }

    /**
     * Приватный метод для получения координат города по его названию.
     *
     * @param city  Название города.
     * @param token Токен для доступа к API геоданных.
     * @return Координаты города.
     * @throws IOException Возникает в случае ошибки при взаимодействии с API.
     */
    private Geo getGeoFromCity(String city, String token) throws IOException {
        String url = new StringBuilder().append(WeatherServiceImpl.GEO_API)
                .append("q=")
                .append(city)
                .append("&limit=1")
                .append("&appid=")
                .append(token).toString();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return objectMapper.readValue(response.body().byteStream(), Geo[].class)[0];
    }

    /**
     * Приватный метод для получения информации о погоде из JSON.
     *
     * @param data JSON с информацией о погоде.
     * @return Объект с информацией о погоде.
     */
    private Weather getWeatherFromJson(JsonNode data) {
        try {
            return objectMapper.treeToValue(data.get("weather").get(0), Weather.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Приватный метод для обновления информации о погоде.
     */
    private void updateWeatherData() {
        List<WeatherData> allWeatherData = storage.getAllWeatherData();
        allWeatherData
                .forEach(w -> {
                    WeatherData weather = getWeatherFromApi(w.getName());
                    weather.setId(w.getId());
                    storage.save(weather);
                });
        log.info("{} данных о погоде обновлены", allWeatherData.size());
    }
}




