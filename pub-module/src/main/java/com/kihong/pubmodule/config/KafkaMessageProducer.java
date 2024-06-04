package com.kihong.pubmodule.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.pubmodule.domain.KafkaMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaMessageProducer {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;


    public KafkaMessageProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
        objectMapper = new ObjectMapper();
    }

    public <T> void sendMessage(String topic, KafkaMessage<T> kafkaMessage) throws JsonProcessingException {
        String value = objectMapper.writeValueAsString(kafkaMessage);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, kafkaMessage.getEntityId(), value);
        producer.send(record);
    }

}
