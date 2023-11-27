package com.myweather.api.integration;

import com.myweather.api.exceptions.ApiCallException;
import com.myweather.api.exceptions.CityNotFoundException;
import com.myweather.api.model.WeatherResponse;
import com.myweather.api.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceIntegrationE2ETest {

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?appid=";
    private static final String API_KEY = "91a5e3d94708c57e8248a454d817a493";

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        weatherService = new WeatherService(HttpClient.newBuilder().build(), API_KEY, API_URL);
    }

    @Test
    public void testGetWeatherByCityWhenCityIsFoundAndApiCallIsSuccessfulThenReturnWeatherResponse() throws IOException, InterruptedException {
        WeatherResponse weatherResponse = weatherService.getWeatherByCity("London");
        assertNotNull(weatherResponse);
        assertEquals("200", weatherResponse.getCod());
    }

    @Test
    public void testGetWeatherByCityWhenCityIsNotFoundThenThrowCityNotFoundException() throws CityNotFoundException {
        assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherByCity("UnknownCity"));
    }

    @Test
    public void testGetWeatherByCityWhenApiCallErrorThenThrowApiCallException() throws ApiCallException {
        assertThrows(ApiCallException.class, () -> weatherService.getWeatherByCity(""));
    }
}
