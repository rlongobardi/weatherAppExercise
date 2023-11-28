package com.myweather.api.controller;

import com.myweather.api.exceptions.ApiCallException;
import com.myweather.api.exceptions.CityNotFoundException;
import com.myweather.api.exceptions.ResponseParsingException;
import com.myweather.api.model.ApiCall;
import com.myweather.api.model.WeatherResponse;
import com.myweather.api.service.HistoryService;
import com.myweather.api.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final HistoryService historyService;

    public WeatherController(WeatherService weatherService, HistoryService historyService) {
        this.weatherService = weatherService;
        this.historyService = historyService;
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<WeatherResponse> getWeatherByCity(@Valid @Size(min = 3, max = 100) @PathVariable String city) {
        log.info("Received request to get weather for city: {}", city);
        WeatherResponse weather = weatherService.getWeatherByCity(city);
        return ResponseEntity.ok(weather);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ApiCall>> getHistory(
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "10") int limit) {
        final List<ApiCall> history = historyService.getHistory(order, limit);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @ExceptionHandler({ResponseParsingException.class})
    public ResponseEntity<String> handleApiCallAndCityNotFoundExceptions(Exception e) {
        log.error("Error in WeatherService", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<String> handleCityNotFoundException(CityNotFoundException e) {
        log.error("City not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ApiCallException.class)
    public ResponseEntity<String> handleCityNotFoundException(ApiCallException e) {
        log.error("City not found", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}