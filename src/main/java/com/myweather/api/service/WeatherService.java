package com.myweather.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweather.api.exceptions.WeatherServiceException;
import com.myweather.api.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class WeatherService {

    private final HttpClient httpClient;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    public WeatherService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public WeatherResponse getWeather(String city) {
        log.info("Getting weather for city: {}", city);
        try {
            String url = apiUrl + apiKey + "&q=" + city;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return parseWeatherResponse(response.body());
        } catch (IOException | InterruptedException e) {
            log.error("Error calling weather API", e);
            throw new WeatherServiceException("Error calling weather API", e);
        }
    }

    private WeatherResponse parseWeatherResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, WeatherResponse.class);
        } catch (IOException e) {
            log.error("Error parsing weather response", e);
            throw new WeatherServiceException("Error parsing weather response", e);
        }
    }
}