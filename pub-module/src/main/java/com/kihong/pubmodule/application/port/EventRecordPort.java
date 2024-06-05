package com.kihong.pubmodule.application.port;

import com.kihong.pubmodule.domain.EventRecord;

import java.util.Optional;

public interface EventRecordPort {

    void save(EventRecord record);
    void published(EventRecord eventRecord);
    Optional<EventRecord> findUnPublishedById(String transactionId);

}
