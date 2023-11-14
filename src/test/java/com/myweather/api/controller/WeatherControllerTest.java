package com.myweather.api.controller;

import com.myweather.api.exceptions.WeatherServiceException;
import com.myweather.api.model.WeatherResponse;
import com.myweather.api.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testGetWeatherWhenCityIsValidThenReturnWeatherResponse() throws Exception {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setCod("200");
        when(weatherService.getWeather(anyString())).thenReturn(weatherResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/{city}", "London")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"cod\":\"200\"}"));
    }

    @Test
    public void testGetWeatherWhenCityIsInvalidThenReturnInternalServerError() throws Exception {
        when(weatherService.getWeather(anyString())).thenThrow(new WeatherServiceException("City not found", new RuntimeException()));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/{city}", "InvalidCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("City not found"));
    }

    @Test
    public void testGetWeatherWhenCityNotFoundThenReturnNotFound() throws Exception {
        String responseBody = "{\"cod\":\"404\",\"message\":\"city not found\"}";
        when(weatherService.getWeather(anyString())).thenThrow(new WeatherServiceException(responseBody, new WeatherServiceException("City not found", new RuntimeException())));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/{city}", "London123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseBody));
    }

    @Test
    public void testGetWeatherWhenCityIsMissingThenReturnBadRequest() throws Exception {
        String responseBody = "{\"cod\":\"400\",\"message\":\"Nothing to geocode\"}";
        when(weatherService.getWeather(anyString())).thenThrow(new WeatherServiceException(responseBody, new RuntimeException()));
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/weather/")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseBody));
    }


}