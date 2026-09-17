package com.example.incident_management.controller;

import com.example.incident_management.entity.AuditLog;
import com.example.incident_management.entity.Incident;
import com.example.incident_management.entity.IncidentPriority;
import com.example.incident_management.entity.IncidentStatus;
import com.example.incident_management.repository.AuditLogRepository;
import com.example.incident_management.repository.IncidentRepository;
import com.example.incident_management.service.AuditLogService;
import com.example.incident_management.service.IncidentService;
import org.springframework.security.core.Authentication;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {
    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private AuditLogService auditLogService;


    @PostMapping
    public ResponseEntity<Incident> createIncident(
            @RequestBody Incident incident,
            Authentication authentication) {

        String username = authentication.getName();

        Incident savedIncident =
                incidentService.createIncident(incident, username);

        return new ResponseEntity<>(
                savedIncident,
                HttpStatus.CREATED
        );
    }
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {

        List<Incident> incidents =
                incidentService.getAllIncidents();

        return new ResponseEntity<>(
                incidents,
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIncidentById(
            @PathVariable ObjectId id) {

        Incident incident =
                incidentService.getIncidentById(id);

        if (incident == null) {
            return new ResponseEntity<>(
                    "Incident not found",
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                incident,
                HttpStatus.OK
        );
    }
//Filter
    @GetMapping("/filter/status")
    public ResponseEntity<?> getIncidentsByStatus(
            @RequestParam IncidentStatus status) {

        return new ResponseEntity<>(
                incidentService.getIncidentsByStatus(status),
                HttpStatus.OK
        );
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<?> getIncidentsByPriority(
            @RequestParam IncidentPriority priority) {

        return new ResponseEntity<>(
                incidentService.getIncidentsByPriority(priority),
                HttpStatus.OK
        );
    }

    @GetMapping("/filter/developer")
    public ResponseEntity<?> getIncidentsByDeveloper(
            @RequestParam String developer) {

        return new ResponseEntity<>(
                incidentService.getIncidentsByDeveloper(developer),
                HttpStatus.OK
        );
    }

    @GetMapping("/filter/reporter")
    public ResponseEntity<?> getIncidentsByReporter(
            @RequestParam String reporter) {

        return new ResponseEntity<>(
                incidentService.getIncidentsByReporter(reporter),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignIncident(
            @PathVariable ObjectId id,
            @RequestParam String developer,
            Authentication authentication) {
        try {
            String assignedBy = authentication.getName();
            Incident incident =
                    incidentService.assignIncident(id, developer, assignedBy);

            if (incident == null) {
                return new ResponseEntity<>("Incident not found", HttpStatus.NOT_FOUND
                );
            }

            return new ResponseEntity<>(incident, HttpStatus.OK
            );

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable ObjectId id,
            @RequestParam IncidentStatus status,
            Authentication authentication) {

        try {
            String username = authentication.getName();
            Incident incident = incidentService.updateStatus(id, status,username);

            if (incident == null) {

                return new ResponseEntity<>("Incident not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(incident, HttpStatus.OK);

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<?> getIncidentHistory(
            @PathVariable ObjectId id) {

        List<AuditLog> history =
                auditLogService.getIncidentHistory(id);

        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
