package com.myweather.api.service;

import com.myweather.api.model.ApiCall;
import com.myweather.api.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void addApiCall(String endpoint) {
        historyRepository.addApiCall(endpoint);
    }

    public List<ApiCall> getHistory(String order, int limit) {
        return historyRepository.getHistory(order, limit);
    }
}