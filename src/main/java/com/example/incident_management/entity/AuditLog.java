package com.example.incident_management.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "audit-logs")
public class AuditLog {

    @Id
    private ObjectId id;

    private ObjectId incidentId;

    private String incidentNumber;

    private String action;

    private String performedBy;

    private String details;

    private LocalDateTime timestamp;
}
