package com.myweather.api.repository;

import com.myweather.api.model.ApiCall;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class HistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public HistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addApiCall(String endpoint) {
        jdbcTemplate.update("INSERT INTO api_call (endpoint, timestamp) VALUES (?, ?)", endpoint, LocalDateTime.now());
    }

    public List<ApiCall> getHistory(String order, int limit) {
        return jdbcTemplate.query("SELECT * FROM api_call ORDER BY timestamp " + order + " LIMIT " + limit,
                (rs, rowNum) -> new ApiCall(rs.getString("endpoint"), rs.getTimestamp("timestamp").toLocalDateTime()));
    }
}