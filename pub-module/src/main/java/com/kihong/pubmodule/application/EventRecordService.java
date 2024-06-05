package com.kihong.pubmodule.application;

import com.kihong.pubmodule.application.port.EventRecordPort;
import com.kihong.pubmodule.domain.EventRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRecordService {

    private final EventRecordPort eventRecordPort;

    public void recordEvent(EventRecord eventRecord) {
        eventRecordPort.save(eventRecord);
    }
    
    public void publishRecord(EventRecord eventRecord) {
        eventRecordPort.published(eventRecord);
    }

    public Optional<EventRecord> findRecordEvent(String transactionId) {
        return eventRecordPort.findUnPublishedById(transactionId);
    }
}
