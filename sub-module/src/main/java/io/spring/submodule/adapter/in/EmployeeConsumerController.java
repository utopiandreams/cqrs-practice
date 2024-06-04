package io.spring.submodule.adapter.in;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeConsumerController {

    @KafkaListener(topics = "employee", groupId = "test")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("record = %s%n" + record.value());
    }

}
