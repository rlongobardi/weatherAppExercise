package com.myweather.api.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testGetWeatherByCity() {
        RestAssured.baseURI = "http://localhost:" + port;

        Response response = RestAssured.given()
                .when()
                .get("/weather/city/London")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertThat(response.jsonPath().getString("city"), is("London"));
    }
}