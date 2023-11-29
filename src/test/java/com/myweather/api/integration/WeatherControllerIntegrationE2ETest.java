package com.myweather.api.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerIntegrationE2ETest {

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
                .statusCode(HttpStatus.OK.value())
                .body("city.name", equalTo(cityName))
                .body("city.population", notNullValue())
                .body("list[0].main.humidity", notNullValue())
                .body("list[0].main.temp", notNullValue());
    }

    @Test
    public void testGetHistory() {
        String cityName = "London";

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/city/" + cityName)
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/history")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThan(0))
                .body("history.endpoint", notNullValue())
                .body("history.timestamp", notNullValue());
    }

    @Test
    public void testGetWeatherByCityNotFound() {
        String cityName = "NonExistentCity";
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/city/" + cityName)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error", equalTo("city not found: NonExistentCity"));
    }

    @Test
    public void testGetHistoryNotFound() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/weather/history/nonexistent")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error", equalTo("Not Found"));
    }
}