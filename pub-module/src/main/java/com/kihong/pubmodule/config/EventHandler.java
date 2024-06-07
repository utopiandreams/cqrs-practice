package com.kihong.pubmodule.config;

import com.kihong.pubmodule.application.EventRecordService;
import com.kihong.pubmodule.domain.EventRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandler {

    private final EventRecordService eventRecordService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEmployeeEvent(EventRecord event) {
        log.info("BEFORE_COMMIT event: {}", event);
        eventRecordService.recordEvent(event);
    }

    /*
     * 버전 3 에서는 메세지를 직접 발행 처리 하지 않고
     * 데이터베이스 변경 log 를 읽어서 직접 변경 사항을 카프카 메세지로 넣어줍니다.(CDC)
     */

}
