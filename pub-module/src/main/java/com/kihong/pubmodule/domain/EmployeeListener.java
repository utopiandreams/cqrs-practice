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

    /*
     * 아래 이벤트 발행 과정은 DB 트랜잭션과 묶이기 때문에
     * 메세지 발행의 결과에 따라 전체 트랜잭션의 성공 여부가 결정되고,
     * 결국 부가적인 기능이 핵심 기능에 영향을 끼치는 문제를 초래합니다.
     */
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
