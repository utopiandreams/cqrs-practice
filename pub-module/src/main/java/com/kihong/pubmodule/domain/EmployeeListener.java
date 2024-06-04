package com.kihong.pubmodule.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kihong.pubmodule.config.KafkaMessageProducer;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeListener {

    private final KafkaMessageProducer messageProducer;

    @PrePersist
    public void prePersist(Employee user) throws JsonProcessingException {
        publishEvent("CREATE", user);
    }

    @PostUpdate
    public void postUpdate(Employee user) throws JsonProcessingException {
        publishEvent("UPDATE", user);
    }

    @PreRemove
    public void preRemove(Employee user) throws JsonProcessingException {
        publishEvent("DELETE", user);
    }

    private void publishEvent(String eventType, Employee employee) throws JsonProcessingException {
        KafkaMessage<Employee> kafkaMessage = createMessage(eventType, employee);
        messageProducer.sendMessage("employee", kafkaMessage);
    }

    private KafkaMessage<Employee> createMessage(String eventType, Employee employee) {
        return KafkaMessage.<Employee>builder()
                .eventType(eventType)
                .entityType("EMPLOYEE")
                .entityId(employee.getId().toString())
                .data(employee)
                .build();
    }



}
