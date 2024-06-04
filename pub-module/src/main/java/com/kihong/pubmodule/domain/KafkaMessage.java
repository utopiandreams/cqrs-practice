package com.kihong.pubmodule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KafkaMessage<T> {
    private String eventType;
    private String entityType;
    private String entityId;
    private T data;
}
