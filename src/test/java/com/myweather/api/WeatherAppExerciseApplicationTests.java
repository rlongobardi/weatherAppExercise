package com.myweather.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class WeatherAppExerciseApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void contextLoads() {
    }

    @Test
    public void actuatorHealthEndpointShouldReturn200() {
        webTestClient.get()
                .uri("http://localhost:" + port + "/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void actuatorInfoEndpointShouldReturn200() {
        webTestClient.get()
                .uri("http://localhost:" + port + "/actuator/info")
                .exchange()
                .expectStatus().isOk();
    }
}