package com.example.siem.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.Transport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.siem.model.LogEvent;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class LogService {

    private final ElasticsearchClient esClient;
    private final String indexName;

    public LogService(@Value("${elastic.host}") String elasticHost, @Value("${elastic.index}") String indexName) {
        RestClient restClient = RestClient.builder(HttpHost.create(elasticHost)).build();
        Transport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.esClient = new ElasticsearchClient(transport);
        this.indexName = indexName;
    }

    public void indexEvent(LogEvent event) {
        try {
            if (event.getTimestamp() == null) event.setTimestamp(Instant.now());
            if (event.getId() == null) event.setId(UUID.randomUUID().toString());
            esClient.index(i -> i.index(indexName).id(event.getId()).document(event));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
