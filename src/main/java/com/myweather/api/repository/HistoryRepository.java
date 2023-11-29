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
        String sql = "INSERT INTO api_call (endpoint, timestamp) VALUES (?, ?)";
        jdbcTemplate.update(sql, endpoint, LocalDateTime.now());
    }

    public List<ApiCall> getHistory(String order, int limit) {
        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            throw new IllegalArgumentException("Invalid order parameter");
        }
        String sql = "SELECT * FROM api_call ORDER BY timestamp " + order + " LIMIT ?";
        return jdbcTemplate.query(sql, new Object[]{limit},
                (rs, rowNum) -> new ApiCall(rs.getString("endpoint"), rs.getTimestamp("timestamp").toLocalDateTime()));
    }
}