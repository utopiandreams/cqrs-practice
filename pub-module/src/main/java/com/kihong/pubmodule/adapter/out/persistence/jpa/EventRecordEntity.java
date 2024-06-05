package com.kihong.pubmodule.adapter.out.persistence.jpa;

import com.kihong.pubmodule.domain.EventRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class EventRecordEntity {

    @Id @GeneratedValue
    private Long id;
    private String transactionId;
    // 누가
    private String entityType;
    private String entityId;

    // 무엇을
    private String eventType;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String data;

    // 메세지 발행 여부
    private String isPublished;
    private Long createdAt;

    public static EventRecordEntity fromDomain(EventRecord eventRecord) {
        return EventRecordEntity.builder()
                .transactionId(eventRecord.getTransactionId())
                .entityType(eventRecord.getEntityType())
                .entityId(eventRecord.getEntityId())
                .eventType(eventRecord.getEventType())
                .data(eventRecord.getData())
                .isPublished("N")
                .createdAt(eventRecord.getCreateAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();
    }

    public void published(){
        isPublished = "Y";
    }

}
