package com.example.incident_management.service;

import com.example.incident_management.entity.Incident;
import com.example.incident_management.entity.IncidentStatus;
import com.example.incident_management.entity.User;
import com.example.incident_management.repository.IncidentRepository;
import com.example.incident_management.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public Incident createIncident(Incident incident, String username) {

        incident.setIncidentNumber(generateIncidentNumber());
        incident.setReportedBy(username);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedAt(LocalDateTime.now());
        incident.setUpdatedAt(LocalDateTime.now());

        Incident savedIncident = incidentRepository.save(incident);

        // Create audit log
        auditLogService.logAction(
                savedIncident.getId(),
                savedIncident.getIncidentNumber(),
                "CREATED",
                username,
                "Incident created"
        );

        return savedIncident;
    }
    private String generateIncidentNumber() {

        long count = incidentRepository.count() + 1;
        return "INC-" + (1000 + count);
    }

    public List<Incident> getAllIncidents() {

        return incidentRepository.findAll();
    }

    public Incident getIncidentById(ObjectId id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public List<Incident> getIncidentsByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status);
    }

    public List<Incident> getIncidentsByPriority(
            com.example.incident_management.entity.IncidentPriority priority) {

        return incidentRepository.findByPriority(priority);
    }

    public List<Incident> getIncidentsByDeveloper(String developer) {
        return incidentRepository.findByAssignedTo(developer);
    }

    public List<Incident> getIncidentsByReporter(String reporter) {
        return incidentRepository.findByReportedBy(reporter);
    }

    // ASSIGN INCIDENT
    public Incident assignIncident(ObjectId id, String developer,String assignedBy) {

        Incident incident = incidentRepository.findById(id).orElse(null);

        if (incident == null) {
            return null;
        }

        // Find logged-in user who is assigning the incident
        User assigningUser =
                userRepository.findByUserName(assignedBy).orElse(null);

        if (assigningUser == null) {
            throw new IllegalArgumentException(
                    "User not found: " + assignedBy
            );
        }

        // Check role of person assigning the incident
        boolean isLead =
                assigningUser.getRoles() != null &&
                        assigningUser.getRoles().contains("LEAD");

        boolean isScrumMaster =
                assigningUser.getRoles() != null &&
                        assigningUser.getRoles().contains("SCRUM_MASTER");

        // Only Lead or Scrum Master can assign
        if (!isLead && !isScrumMaster) {
            throw new IllegalArgumentException(
                    "Only LEAD or SCRUM_MASTER can assign incidents"
            );
        }
        // Find developer in users collection
        User devUser = userRepository.findByUserName(developer).orElse(null);

        if (devUser == null) {
            throw new IllegalArgumentException(
                    "Developer not found: " + developer
            );
        }

        // Check developer role
        if (devUser.getRoles() == null ||
                !devUser.getRoles().contains("DEVELOPER")) {

            throw new IllegalArgumentException(
                    "This User is not a DEVELOPER: " + developer
            );
        }

        String oldStatus = incident.getStatus().name();

        incident.setAssignedTo(developer);
        incident.setStatus(IncidentStatus.ASSIGNED);
        incident.setUpdatedAt(LocalDateTime.now());

        Incident updatedIncident = incidentRepository.save(incident);

        // Create audit log
        auditLogService.logAction(
                updatedIncident.getId(),
                updatedIncident.getIncidentNumber(),
                "ASSIGNED",
                assigningUser.getUserName(),
                "Incident assigned to " + developer
        );

        // Log status change
        auditLogService.logAction(
                updatedIncident.getId(),
                updatedIncident.getIncidentNumber(),
                "STATUS_CHANGED",
                developer,
                "Status changed from " + oldStatus + " to ASSIGNED"
        );

        // Email notification
        emailService.sendEmail(
                devUser.getEmail(),
                "Incident Assigned: " + updatedIncident.getIncidentNumber(),
                "Hello " + devUser.getUserName() + ",\n\n"
                        + "A new incident has been assigned to you.\n\n"
                        + "Incident Number: "
                        + updatedIncident.getIncidentNumber() + "\n"
                        + "Title: "
                        + updatedIncident.getTitle() + "\n"
                        + "Priority: "
                        + updatedIncident.getPriority() + "\n"
                        + "Status: "
                        + updatedIncident.getStatus() + "\n\n"
                        + "Please review and take the necessary action.\n\n"
                        + "Regards,\n"
                        + "Incident Management System"
        );
        return updatedIncident;
    }


    // UPDATE STATUS
    public Incident updateStatus(ObjectId id, IncidentStatus newStatus, String username) {

        Incident incident = incidentRepository.findById(id).orElse(null);

        if (incident == null) {
            return null;
        }
        // Find logged-in user
        User user = userRepository.findByUserName(username).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException(
                    "User not found: " + username
            );
        }

        // Check whether user has a valid role
        boolean isDeveloper =
                user.getRoles().contains("DEVELOPER");
        boolean isLead =
                user.getRoles().contains("LEAD");
        boolean isScrumMaster =
                user.getRoles().contains("SCRUM_MASTER");

        if (!isDeveloper && !isLead && !isScrumMaster) {
            throw new IllegalArgumentException(
                    "User does not have permission to update incident status"
            );
        }

        // Developer can update only incidents assigned to them
        if (isDeveloper && !isLead && !isScrumMaster) {

            if (incident.getAssignedTo() == null ||
                    !incident.getAssignedTo().equals(username)) {

                throw new IllegalArgumentException(
                        "Developer can update status only for incidents assigned to them"
                );
            }
        }

        IncidentStatus currentStatus = incident.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {

            throw new IllegalStateException(
                    "Invalid status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }

        incident.setStatus(newStatus);
        incident.setUpdatedAt(LocalDateTime.now());

        Incident updatedIncident = incidentRepository.save(incident);

        // Create audit log
        auditLogService.logAction(
                updatedIncident.getId(),
                updatedIncident.getIncidentNumber(),
                "STATUS_CHANGED",
                incident.getAssignedTo(),
                "Status changed from "
                        + currentStatus
                        + " to "
                        + newStatus
        );

        return updatedIncident;
    }

    private boolean isValidTransition(
            IncidentStatus current,
            IncidentStatus next) {

        return switch (current) {

            case OPEN ->
                    next == IncidentStatus.ASSIGNED;

            case ASSIGNED ->
                    next == IncidentStatus.IN_PROGRESS;

            case IN_PROGRESS ->
                    next == IncidentStatus.RESOLVED;

            case RESOLVED ->
                    next == IncidentStatus.CLOSED;

            case CLOSED ->
                    false;
        };
    }
}
