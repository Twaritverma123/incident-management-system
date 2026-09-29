package com.example.incident_management.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String route() {
        return """
                <html>
                    <body>
                        <h2>Incident Management Application</h2>
                        <a href="https://github.com/Twaritverma123/Journal_application_JWT" target="_blank">
                            View GitHub Repository
                        </a>
                    </body>
                </html>
                """;
    }
}