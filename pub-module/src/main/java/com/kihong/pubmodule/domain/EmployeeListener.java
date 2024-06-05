package com.kihong.pubmodule.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.pubmodule.config.EventPublisher;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmployeeListener {

    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @PrePersist
    public void prePersist(Employee employee) throws JsonProcessingException {
        eventPublisher.publishEvent(createEvent("CREATE", employee));
    }

    @PostUpdate
    public void postUpdate(Employee employee) throws JsonProcessingException {
        eventPublisher.publishEvent(createEvent("UPDATE", employee));
    }

    @PreRemove
    public void preRemove(Employee employee) throws JsonProcessingException {
        eventPublisher.publishEvent(createEvent("DELETE", employee));
    }

    public EventRecord createEvent(String eventType, Employee employee) throws JsonProcessingException {
        return new EventRecord(
                this,
                UUID.randomUUID().toString(),
                "employee",
                employee.getId().toString(),
                eventType,
                objectMapper.writeValueAsString(employee),
                LocalDateTime.now(),
                false
        );
    }

}
