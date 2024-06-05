package com.kihong.pubmodule.domain;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"data"})
public class KafkaMessage {
    private String id;
    private String eventType;
    private String entityType;
    private String entityId;
    private Long createAt;
    private String data;
}
