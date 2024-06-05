package com.kihong.pubmodule.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kihong.pubmodule.application.EventRecordService;
import com.kihong.pubmodule.domain.EventRecord;
import com.kihong.pubmodule.domain.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final EventRecordService eventRecordService;
    private final KafkaMessageProducer kafkaMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEmployeeEvent(EventRecord event) {
        log.info("BEFORE_COMMIT event: {}", event);
        eventRecordService.recordEvent(event);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTransaction(EventRecord event) throws JsonProcessingException {
        var eventRecordOpt = eventRecordService.findRecordEvent(event.getTransactionId());
        if(eventRecordOpt.isEmpty()) {
            return;
        }
        EventRecord eventRecord = eventRecordOpt.get();
        KafkaMessage kafkaMessage = KafkaMessage.builder()
                .eventType(eventRecord.getEventType())
                .entityType(event.getEventType())
                .entityId(event.getEntityId())
                .data(event.getData())
                .createAt(event.getCreateAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();
        kafkaMessageProducer.sendMessage("employee", kafkaMessage);
        eventRecordService.publishRecord(eventRecord);
        log.info("AFTER_COMMIT kafkaMessage: {}", kafkaMessage);
    }

}
