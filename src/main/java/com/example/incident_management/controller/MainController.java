package com.example.incident_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String route(){
        return "https://github.com/Twaritverma123/Journal_application_JWT";
    }
}
