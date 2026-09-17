package com.example.incident_management.repository;

import com.example.incident_management.entity.AuditLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogRepository extends MongoRepository<AuditLog, ObjectId> {

    List<AuditLog> findByIncidentIdOrderByTimestampAsc(ObjectId incidentId);
}
