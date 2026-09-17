package com.example.incident_management.repository;

import com.example.incident_management.entity.Incident;
import com.example.incident_management.entity.IncidentPriority;
import com.example.incident_management.entity.IncidentStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IncidentRepository extends MongoRepository<Incident, ObjectId> {

    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findByPriority(IncidentPriority priority);

    List<Incident> findByAssignedTo(String assignedTo);

    List<Incident> findByReportedBy(String reportedBy);
}
