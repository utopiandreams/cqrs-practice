# 1. 개요
   Apache Kafka 및 Event-Driven Architecture 이해를 위하여 간단하게 예제를 작성하였습니다.

utopiandreams/cqrs-practice

test-container 에 있는 docker-compose 를 실행하여 환경을 구성한 뒤 실행해보실 수 있습니다.

# 2. 예제 설명
   기능 : Employee 테이블에 대하여 Read, Create 수행

CQRS 패턴을 적용, 쓰기DB 는 Mysql, 읽기DB 는 MongoDB 를 통해 수행합니다.

두 DB 간의 eventual consistency (결과적 일관성) 을 보장하기 위하여 쓰기 행위 발생시 Kafka 로 메세지를 발행, 해당 이벤트를 consumer 서버에서 읽어들여 mongodb 와 동기화합니다.

# 3. 착안 사항
## 1) 버전 1
   버전 1 에서는 Event-Driven Architecture 를 적용해보았습니다. Employee 테이블에 변화가 생기면 이를 추적하는 Listener 객체가 이를 곧바로 Kafka 메세지로 변환하여 메세지를 발행합니다.

```java
@Component
@RequiredArgsConstructor
public class EmployeeListener {

    private final KafkaMessageProducer messageProducer;

    @PrePersist
    public void prePersist(Employee user) throws JsonProcessingException {
        publishEvent("CREATE", user);
    }

    @PostUpdate
    public void postUpdate(Employee user) throws JsonProcessingException {
        publishEvent("UPDATE", user);
    }

    @PreRemove
    public void preRemove(Employee user) throws JsonProcessingException {
        publishEvent("DELETE", user);
    }

    /*
     * 아래 이벤트 발행 과정은 DB 트랜잭션과 묶이기 때문에
     * 메세지 발행의 결과에 따라 전체 트랜잭션의 성공 여부가 결정되고,
     * 결국 부가적인 기능이 핵심 기능에 영향을 끼치는 문제를 초래합니다.
     */
    private void publishEvent(String eventType, Employee employee) throws JsonProcessingException {
        KafkaMessage<Employee> kafkaMessage = createMessage(eventType, employee);
        messageProducer.sendMessage("employee", kafkaMessage);
    }

    private KafkaMessage<Employee> createMessage(String eventType, Employee employee) {
        return KafkaMessage.<Employee>builder()
                .eventType(eventType)
                .entityType("EMPLOYEE")
                .entityId(employee.getId().toString())
                .data(employee)
                .build();
    }

}
```

이후 토픽을 구독하고 있는 sub-module (메세지 구독 모듈) 에서 메세지를 읽어들이고, MongoDB 에 동기화 합니다.
```java
@Controller
@RequiredArgsConstructor
@Slf4j
public class EmployeeConsumerController {
    private final EmployeeService employeeService;

    @KafkaListener(topics = "employee", groupId = "mongo")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received record: {}", record);
        employeeService.save(record.value());
    }

}
```

위에 주석에 나와 있다시피 메세지 발행 과정이 트랜잭션 내부에서 일어나기 때문에 완전한 원자성을 보장하지만, 메세지 발행이라는 부가 기능이 핵심 기능을 차단하게 만드는 문제가 발생합니다. 그러나 트랜잭션 이후에 메세지 발행을 하게 되면 메세지 발행이 보장되지 못하기 때문에 consistency 가 깨지게 됩니다.

## 2) 버전 2
   버전 2 에서는 Transactional outbox pattern 을 적용하여 버전 1 의 문제 상황을 해결합니다.

버전 2 에서는 핵심기능이 이뤄지는 트랜잭션 내부에서 EventRecord 를 저장합니다. EventRecord 는 핵심 기능이 이루어지는 DB 와 동일한 DB 에 저장되기 때문에 원자성이 보장됩니다. 또한, EventRecord 에는 이벤트 정보와 더불어 메세지가 발행되었는지를 기록하는 isPublished 컬럼을 가지고 있기 때문에, 이후 메세지 발행을 보장할 수 있습니다.

트랜잭션 종료 되면, 새로운 트랜잭션에서 EventRecord 를 기반으로 메세지 발행을 시도 하기 때문에, 핵심 기능과 메세지 발행이라는 부가 기능의 결합이 끊어지게 됩니다.

버전 2 에서도 Employee 객체에 변화를 추적하여 이벤트를 발행합니다. (카프카 메세지 발행 아님)
```java
@Component
@RequiredArgsConstructor
public class EmployeeListener {

    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @PrePersist
    public void prePersist(Employee employee) throws JsonProcessingException {
        eventPublisher.publishEvent(createEvent("CREATE", employee));
    }

    @PostUpdate
    public void postUpdate(Employee employee) throws JsonProcessingException {
        eventPublisher.publishEvent(createEvent("UPDATE", employee));
    }

    @PreRemove
    public void preRemove(Employee employee) throws JsonProcessingException {
        eventPublisher.publishEvent(createEvent("DELETE", employee));
    }

    public EventRecord createEvent(String eventType, Employee employee) throws JsonProcessingException {
        return new EventRecord(
                this,
                UUID.randomUUID().toString(),
                "employee",
                employee.getId().toString(),
                eventType,
                objectMapper.writeValueAsString(employee),
                LocalDateTime.now(),
                false
        );
    }

}
```

발행된 이벤트는 아래와 같이 이벤트 핸들러에 의해 처리됩니다.

```java
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
```

@TransactionalEventListener 어노테이션을 이용하여 커밋 이전에 EventRecord 를 DB 에 기록하고, 커밋 이후에 비동기적으로 새로운 트랜잭션을 실행하면서 해당 이벤트를 읽어와서 메세지 발행을 시도합니다. 만약 메세지 발행이 실패하게 되면, EventRecord 의 isPublished 컬럼이 N 으로 남아 있기 때문에 이후에 재시도를 할 수 있습니다. 또한, 메세지 발행이 Y 로 변환되면 메세지 발행이 중복으로 이루어지지 않습니다.

## 3) 버전 3 (계획)
   버전 2 에서는 transactional outbox 패턴을 통해 메세지 발행이 보장되게 하였습니다. 이벤트 발행이 실패하는 경우에도 메세지 발행을 재차 시도하여 eventual consistency 가 보장되게 됩니다.

그러나 메세지 발행이 실패하는 경우 이벤트의 발행 순서가 바뀌게 될 수 있고, 때문에 순차적으로 소비되는 카프카의 메세지 큐에 순서가 뒤바뀐 채로 메세지가 들어가게 되어 문제가 발생할 여지가 있습니다.

이를 해결하기 위해 데이터베이스의 EventRecord 를 생성 순서에 따라 발행처리하는 메세지 릴레이(debezium) 를 도입하였습니다.

debezium 은 데이터베이스의 변경 log 를 순차적으로 읽어서 카프카에 메세지를 전달해주는 Kafka Connect 의 구현체입니다. docker container 를 띄운 후,
curl -X POST -H "Content-Type: application/json" --data @test-container/kafka-connectors/mysql-connector.json http://localhost:8083/connectors
명령어를 입력하여 mysql 커넥터를 등록하면 지정된 mysql 스키마의 변경사항을 추적하여 카프카 메세지를 발행합니다.

