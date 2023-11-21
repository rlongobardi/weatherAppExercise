package com.myweather.api.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void testGetWeatherByCity() {
        String cityName = "London";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/city/" + cityName)
                .then()
                .statusCode(200)
                .body("city", equalTo(cityName))
                .body("temperature", notNullValue())
                .body("humidity", notNullValue());
    }

    @Test
    public void testGetHistory() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/history")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("history[0].city", notNullValue())
                .body("history[0].temperature", notNullValue())
                .body("history[0].humidity", notNullValue());
    }

    @Test
    public void testGetWeatherByCityNotFound() {
        String cityName = "NonExistentCity";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/city/" + cityName)
                .then()
                .statusCode(404)
                .body("error", equalTo("City not found"));
    }

    @Test
    public void testGetHistoryNotFound() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/history/nonexistent")
                .then()
                .statusCode(404)
                .body("error", equalTo("History not found"));
    }
}