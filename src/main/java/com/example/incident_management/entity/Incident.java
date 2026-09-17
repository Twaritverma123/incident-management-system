package com.example.incident_management.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "incidents")
public class Incident {
    @Id
    private ObjectId id;

    private String incidentNumber;

    private String title;

    private String description;

    private IncidentPriority priority;

    private IncidentStatus status;

    private String reportedBy;

    private String assignedTo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
