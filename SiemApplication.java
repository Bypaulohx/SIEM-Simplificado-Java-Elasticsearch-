package com.example.siem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SiemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SiemApplication.class, args);
    }
}
