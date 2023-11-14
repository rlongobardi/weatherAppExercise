package com.myweather.api.service;

import com.myweather.api.exceptions.WeatherServiceException;
import com.myweather.api.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
    }

    @Test
    public void testGetWeatherWhenApiCallIsSuccessfulAndResponseCanBeParsed() throws IOException, InterruptedException {
        String city = "London";
        String responseBody = "{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[],\"city\":{}}";
        when(httpResponse.body()).thenReturn(responseBody);

        WeatherResponse weatherResponse = weatherService.getWeather(city);

        assertNotNull(weatherResponse);
        assertEquals("200", weatherResponse.getCod());
    }

    @Test
    public void testGetWeatherWhenApiCallFails() throws IOException, InterruptedException {
        String city = "London";
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(IOException.class);

        assertThrows(WeatherServiceException.class, () -> weatherService.getWeather(city));
    }

    @Test
    public void testParseWeatherResponseWhenResponseBodyCanBeParsed() {
        String responseBody = "{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[],\"city\":{}}";

        WeatherResponse weatherResponse = weatherService.getWeather(responseBody);

        assertNotNull(weatherResponse);
        assertEquals("200", weatherResponse.getCod());
    }

    @Test
    public void testParseWeatherResponseWhenResponseBodyCannotBeParsed() {
        String responseBody = "invalid json";

        assertThrows(WeatherServiceException.class, () -> weatherService.getWeather(responseBody));
    }
}
