package com.example.siem.controller;

import org.springframework.web.bind.annotation.*;
import com.example.siem.model.Alert;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final ElasticsearchClient esClient;
    private final String alertsIndex;

    public AlertController(ElasticsearchClient esClient, @org.springframework.beans.factory.annotation.Value("${elastic.alerts-index}") String alertsIndex) {
        this.esClient = esClient;
        this.alertsIndex = alertsIndex;
    }

    @GetMapping
    public List<Alert> listAlerts(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "20") int size) throws IOException {
        SearchResponse<Alert> sr = esClient.search(s -> s.index(alertsIndex).from(from).size(size), Alert.class);
        return sr.hits().hits().stream().map(h -> h.source()).collect(Collectors.toList());
    }
}
