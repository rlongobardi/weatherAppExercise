package com.myweather.api.repository;

import com.myweather.api.model.ApiCall;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
public class HistoryRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private HistoryRepository historyRepository;

    @BeforeEach
    public void setUp() {
        historyRepository = new HistoryRepository(jdbcTemplate);
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.update("DELETE FROM api_call");
    }

    @Test
    @Sql("/test-data.sql")
    public void testAddApiCallWhenCalledThenRecordInserted() {
        // Arrange
        String endpoint = "/weather";

        // Act
        historyRepository.addApiCall(endpoint);
        List<ApiCall> apiCalls = historyRepository.getHistory("DESC", 1);

        // Assert
        assertEquals(1, apiCalls.size());
        assertEquals(endpoint, apiCalls.get(0).getEndpoint());
    }

    @Test
    @Sql("/test-data.sql")
    public void testGetHistoryWhenCalledThenRecordsRetrieved() {
        // Arrange
        String endpoint1 = "/weather";
        String endpoint2 = "/forecast";
        historyRepository.addApiCall(endpoint1);
        historyRepository.addApiCall(endpoint2);

        // Act
        List<ApiCall> apiCalls = historyRepository.getHistory("ASC", 2);

        // Assert
        assertEquals(2, apiCalls.size());
        assertEquals(endpoint1, apiCalls.get(0).getEndpoint());
        assertEquals(endpoint2, apiCalls.get(1).getEndpoint());
    }
}
