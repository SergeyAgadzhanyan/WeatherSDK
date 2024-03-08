package org.example.temporary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.example.model.Geo;

import java.io.IOException;

public class API {
    private final String GEO_API = "http://api.openweathermap.org/geo/1.0/direct?";
    private final String DATA_API = "https://api.openweathermap.org/data/3.0/onecall?";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void call() throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = GEO_API +
                "q=Moscow" +
                "&limit=1" +
                "&appid=f1c3ff0a7f316aacf277656819c4a761";
        Request request = new Request.Builder()
                .url(url)
                .build(); // defaults to GET

        Response response = client.newCall(request).execute();

        Geo geo = objectMapper.readValue(response.body().byteStream(), Geo[].class)[0];
        StringBuilder dataUrl = new StringBuilder();
        dataUrl.append(DATA_API)
                .append("lat=")
                .append(geo.getLat())
                .append("&lon=")
                .append(geo.getLon())
                .append("&appid=")
                .append("f1c3ff0a7f316aacf277656819c4a761");
        Request request2 = new Request.Builder()
                .url(dataUrl.toString())
                .build(); // defaults to GET

        Response response2 = client.newCall(request).execute();
        System.out.println(response2.body().string());
    }
}
