package com.myweather.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweather.api.model.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtil {

    public static WeatherResponse getWeatherResponse() {
        try {
            File file = new ClassPathResource("success_response_london.json").getFile();
            return new ObjectMapper().readValue(file, WeatherResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load weather response from JSON file", e);
        }
    }

    public static String convertObjectToJsonString(WeatherResponse weatherResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(weatherResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    @Test
    void testGetWeatherResponseWhenCalledThenReturnsWeatherResponse() {
        WeatherResponse weatherResponse = TestUtil.getWeatherResponse();

        assertNotNull(weatherResponse, "WeatherResponse should not be null");
        assertNotNull(weatherResponse.getCod(), "Cod should not be null");
        assertNotNull(weatherResponse.getMessage(), "Message should not be null");
        assertNotNull(weatherResponse.getCnt(), "Cnt should not be null");
        assertNotNull(weatherResponse.getList(), "List should not be null");
        assertNotNull(weatherResponse.getCity(), "City should not be null");
    }
}