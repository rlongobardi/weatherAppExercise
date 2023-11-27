package com.myweather.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

@Configuration
public class HttpClientConfig {

    @Value("${http.connection.timeout}")
    private int timeout;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeout))
                .executor(Executors.newFixedThreadPool(8))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

}