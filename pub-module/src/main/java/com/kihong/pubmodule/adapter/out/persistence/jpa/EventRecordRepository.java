package com.kihong.pubmodule.adapter.out.persistence.jpa;

import com.kihong.pubmodule.application.port.EventRecordPort;
import com.kihong.pubmodule.domain.EventRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRecordRepository implements EventRecordPort {

    private final EventJpaRepository eventJpaRepository;
    @Override
    public void save(EventRecord record) {
        EventRecordEntity recordEntity = EventRecordEntity.fromDomain(record);
        eventJpaRepository.save(recordEntity);
    }

    @Override
    public void published(EventRecord eventRecord) {
        EventRecordEntity recordEntity = eventJpaRepository.findByTransactionId(eventRecord.getTransactionId())
                .orElseThrow(() -> new RuntimeException("없음"));
        recordEntity.published();
        eventJpaRepository.save(recordEntity);
    }

    @Override
    public Optional<EventRecord> findUnPublishedById(String transactionId) {
        return eventJpaRepository.findByTransactionId(transactionId)
                .flatMap(eventRecordEntity ->
                        Optional.of(new EventRecord(
                                this,
                                eventRecordEntity.getTransactionId(),
                                eventRecordEntity.getEntityType(),
                                eventRecordEntity.getEntityId(),
                                eventRecordEntity.getEventType(),
                                eventRecordEntity.getData(),
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(eventRecordEntity.getCreatedAt()), ZoneId.systemDefault()),
                                false
                        )));
    }
}
