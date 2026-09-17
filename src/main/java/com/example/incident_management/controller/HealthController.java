package com.example.incident_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HealthController {
    @GetMapping("/health")
    public String health() {
     return "Incident Management system is running";
    }

    @GetMapping("/secure-test")
    public String secureTest() {
        return "Authentication successful ";
    }
}
