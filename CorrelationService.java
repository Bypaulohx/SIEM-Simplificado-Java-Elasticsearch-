package com.example.siem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.TermsBucket;

import com.example.siem.model.Alert;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

/*
 Note: This implementation uses a simple approach: it queries for FAILED_LOGIN events
 within the last N minutes and performs a terms aggregation by sourceIp to count occurrences.
 If a bucket count >= threshold, it indexes an alert into alerts-index.
 The exact client aggregation API may require adjustment depending on the elasticsearch-java version.
*/

@Service
public class CorrelationService {

    private final ElasticsearchClient esClient;
    private final String indexName;
    private final String alertsIndex;

    public CorrelationService(ElasticsearchClient esClient, @Value("${elastic.index}") String indexName, @Value("${elastic.alerts-index}") String alertsIndex) {
        this.esClient = esClient;
        this.indexName = indexName;
        this.alertsIndex = alertsIndex;
    }

    // run every 30 seconds
    @Scheduled(fixedDelay = 30000)
    public void runRules() {
        try {
            Instant now = Instant.now();
            Instant from = now.minus(10, ChronoUnit.MINUTES);

            // Build a query: eventType:FAILED_LOGIN and timestamp >= from
            Query q = Query.of(b -> b
                    .bool(bb -> bb
                            .must(m -> m.term(t -> t.field("eventType").value(v -> v.stringValue("FAILED_LOGIN"))))
                            .must(m2 -> m2.range(r -> r.field("timestamp").gte(JsonData.of(from.toString()))))
                    )
            );

            // Perform search with aggregation by sourceIp
            SearchResponse<Object> resp = esClient.search(s -> s
                            .index(indexName)
                            .size(0)
                            .query(q)
                            .aggregations("by_ip", ag -> ag
                                    .terms(t -> t.field("sourceIp").size(1000).minDocCount(1)))
                    , Object.class);

            Map<String, Aggregate> aggs = resp.aggregations();
            if (aggs != null && aggs.containsKey("by_ip")) {
                Aggregate agg = aggs.get("by_ip");
                TermsAggregate terms = agg.terms();
                if (terms != null && terms.buckets() != null) {
                    for (TermsBucket bucket : terms.buckets().array()) {
                        String ip = bucket.key().stringValue();
                        long count = bucket.docCount().longValue();
                        if (count >= 5) {
                            // create alert
                            Alert alert = new Alert();
                            alert.setId(UUID.randomUUID().toString());
                            alert.setType("BRUTE_FORCE");
                            alert.setSourceIp(ip);
                            alert.setCount((int) count);
                            alert.setStart(from);
                            alert.setEnd(now);
                            alert.setCreatedAt(Instant.now());
                            esClient.index(i -> i.index(alertsIndex).id(alert.getId()).document(alert));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
