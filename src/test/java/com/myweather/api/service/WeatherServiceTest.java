package com.myweather.api.service;

import com.myweather.api.exceptions.ApiCallException;
import com.myweather.api.exceptions.CityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
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
    public void setUp() {
        weatherService = new WeatherService("apiKey", "apiUrl", new HistoryService(null));
    }

    @Test
    public void testGetWeatherByCityWhenCityIsFoundAndApiCallIsSuccessfulThenReturnWeatherResponse() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn("{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[],\"city\":{}}");

        WeatherResponse weatherResponse = weatherService.getWeatherByCity("London");

        assertNotNull(weatherResponse);
        assertEquals("200", weatherResponse.getCod());
    }

    @Test
    public void testGetWeatherByCityWhenCityIsNotFoundThenThrowCityNotFoundException() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(404);

        assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherByCity("UnknownCity"));
    }

    @Test
    public void testGetWeatherByCityWhenApiCallErrorThenThrowApiCallException() throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(IOException.class);

        assertThrows(ApiCallException.class, () -> weatherService.getWeatherByCity("London"));
    }

}
