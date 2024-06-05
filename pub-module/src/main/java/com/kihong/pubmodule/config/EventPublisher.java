package com.kihong.pubmodule.config;

import com.kihong.pubmodule.domain.EventRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(EventRecord eventRecord) {
        applicationEventPublisher.publishEvent(eventRecord);
    }

}
