package com.myweather.api.controller;

import com.myweather.api.exceptions.WeatherServiceException;
import com.myweather.api.model.WeatherResponse;
import com.myweather.api.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Slf4j
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{city}")
    public ResponseEntity<?> getWeather(@Valid @Size(min = 2, max = 30) @PathVariable String city) {
        log.info("Received request to get weather for city: {}", city);
        try {
            WeatherResponse weather = weatherService.getWeather(city);
            return ResponseEntity.ok(weather);
        } catch (WeatherServiceException e) {
            log.error("Error in WeatherService", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ExceptionHandler(WeatherServiceException.class)
    public ResponseEntity<String> handleWeatherServiceException(WeatherServiceException e) {
        log.error("Error in WeatherService", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}