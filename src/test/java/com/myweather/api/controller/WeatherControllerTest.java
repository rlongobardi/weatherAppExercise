package com.myweather.api.controller;

import com.myweather.api.exceptions.CityNotFoundException;
import com.myweather.api.exceptions.ValidationException;
import com.myweather.api.model.WeatherResponse;
import com.myweather.api.service.HistoryService;
import com.myweather.api.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private HistoryService historyService;

    @Test
    public void testGetWeatherByCityWhenValidCityThenReturnWeatherResponse() throws Exception {
        // Arrange
        WeatherResponse weatherResponse = new WeatherResponse();
        when(weatherService.getWeatherByCity(anyString())).thenReturn(weatherResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/city/{city}", "London"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{}"));
    }

    @Test
    public void testGetHistoryWhenDefaultParametersThenReturnApiCallList() throws Exception {
        when(historyService.getHistory(anyString(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/history"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    public void testGetHistoryWhenInvalidParametersThenReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/history")
                        .param("order", "invalid")
                        .param("limit", "A"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void testGetWeatherByCityWhenCityNotFoundThenReturnNotFoundResponse() throws Exception {
        String nonExistentCity = "NonExistentCity";
        when(weatherService.getWeatherByCity(nonExistentCity)).thenThrow(new CityNotFoundException("City not found: " + nonExistentCity));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/city/{city}", nonExistentCity))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("City not found: " + nonExistentCity));
    }

    @Test
    public void testGetWeatherByCityWhenInvalidCityThenReturnBadRequestResponse() throws Exception {
        String invalidCity = "123";
        when(weatherService.getWeatherByCity(invalidCity)).thenThrow(new ValidationException("Invalid city name: " + invalidCity));

        mockMvc.perform(MockMvcRequestBuilders.get("/weather/city/{city}", invalidCity))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Invalid city name: " + invalidCity)));
    }
}