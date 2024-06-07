package io.spring.submodule.adapter.in;

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

    // 카프카 커넥트(Debezium) 로 부터 발행된 메세지 토픽
    @KafkaListener(topics = "mysqldb.test.event_record_entity", groupId = "mongo")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received record: {}", record);
        employeeService.sycn(record.value());
    }





}
