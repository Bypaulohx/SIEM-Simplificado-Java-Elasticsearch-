package com.example.siem.controller;

import com.example.siem.model.LogEvent;
import com.example.siem.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<?> ingest(@RequestBody LogEvent event) {
        logService.indexEvent(event);
        return ResponseEntity.ok().build();
    }
}
