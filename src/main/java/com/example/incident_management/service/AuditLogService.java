package com.example.incident_management.service;

import com.example.incident_management.entity.AuditLog;
import com.example.incident_management.repository.AuditLogRepository;
import com.example.incident_management.repository.IncidentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logAction(
            ObjectId incidentId,
            String incidentNumber,
            String action,
            String performedBy,
            String details) {

        AuditLog auditLog = new AuditLog();

        auditLog.setIncidentId(incidentId);
        auditLog.setIncidentNumber(incidentNumber);
        auditLog.setAction(action);
        auditLog.setPerformedBy(performedBy);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }
    public List<AuditLog> getIncidentHistory(ObjectId incidentId) {
        return auditLogRepository
                .findByIncidentIdOrderByTimestampAsc(incidentId);
    }
}
