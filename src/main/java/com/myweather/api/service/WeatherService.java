package com.myweather.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweather.api.exceptions.ApiCallException;
import com.myweather.api.exceptions.CityNotFoundException;
import com.myweather.api.exceptions.ValidationException;
import com.myweather.api.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String VALID_CITY = "^[a-zA-Z\\s]{3,100}$";
    private final HttpClient httpClient;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    @Autowired
    private final HistoryService historyService;

    @Autowired
    public WeatherService(HttpClient httpClient, @Value("${openweather.api.key}") String apiKey,
                          @Value("${openweather.api.url}") String apiUrl, HistoryService historyService) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.historyService = historyService;
    }

    public WeatherResponse getWeatherByCity(String city) {
        log.info("Getting weather for city: {}", city);

        if (city == null || city.isEmpty() || !city.matches(VALID_CITY)) {
            throw new ValidationException("Invalid city name: " + city);
        }
        try {
            final HttpResponse<String> response = callWeatherApiClient(city);

            if (response.statusCode() == 404) {
                throw new CityNotFoundException("city not found: " + city);
            }

            String url = constructWeatherApiUrl(city);
            historyService.addApiCall(url);

            return parseWeatherResponse(response.body());
        } catch (IOException | InterruptedException e) {
            log.error("Error calling weather API", e);
            throw new ApiCallException("Error calling weather API", e);
        }
    }

    private String constructWeatherApiUrl(String city) {
        return "%s?appid=%s&q=%s".formatted(apiUrl, apiKey, city);
    }

    private HttpResponse<String> callWeatherApiClient(String city) throws IOException, InterruptedException {
        String url = constructWeatherApiUrl(city);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private WeatherResponse parseWeatherResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, WeatherResponse.class);
        } catch (IOException e) {
            log.error("Error parsing weather response", e);
            throw new ApiCallException("Error parsing weather response", e);
        }
    }
}