package com.kihong.pubmodule.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
@ToString(exclude = {"data"})
public class EventRecord extends ApplicationEvent {
    private final String transactionId;

    // 누가
    private final String entityType;
    private final String entityId;

    // 무엇을
    private final String eventType;
    private final String data;

    // 언제
    private final LocalDateTime createAt;

    private final boolean isPublished;

    public EventRecord(Object source, String transactionId, String entityType, String entityId, String eventType, String data, LocalDateTime createAt, boolean isPublished) {
        super(source);
        this.transactionId = transactionId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.eventType = eventType;
        this.data = data;
        this.createAt = createAt;
        this.isPublished = isPublished;
    }

}
