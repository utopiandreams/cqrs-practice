package io.spring.submodule.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.submodule.application.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmployeeConsumerController {
    private final EmployeeService employeeService;

    @KafkaListener(topics = "employee", groupId = "mongo")
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.info("Received record: {}", record);
        employeeService.save(record.value());
    }



}
