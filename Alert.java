package com.example.siem.model;

import java.time.Instant;
import java.util.Map;

public class Alert {
    private String id;
    private String type;
    private String sourceIp;
    private int count;
    private Instant start;
    private Instant end;
    private Instant createdAt;
    private Map<String,Object> details;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public Instant getStart() { return start; }
    public void setStart(Instant start) { this.start = start; }

    public Instant getEnd() { return end; }
    public void setEnd(Instant end) { this.end = end; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
}
