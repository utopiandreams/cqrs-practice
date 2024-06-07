package io.spring.submodule.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.submodule.application.port.EmployeeCommand;
import io.spring.submodule.domain.EmployeeDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeCommand employeeCommand;
    private final ObjectMapper objectMapper;

    @Transactional
    public void sycn(String employeeChanges) {
        try {
            JsonNode employeeJsonNode = objectMapper.readTree(employeeChanges);
            JsonNode payload = employeeJsonNode.get("payload");

            // 변경 후의 데이터
            JsonNode after = payload.get("after");
            String eventType = after.get("event_type").asText();
            EmployeeDocument employeeData = objectMapper.readValue(after.get("data").asText(), EmployeeDocument.class);

            switch (eventType) {
                case "CREATE":
                    employeeCommand.save(employeeData);
                    break;
                case "UPDATE":
                    employeeCommand.update(employeeData);
                    break;
                case "DELETE":
                    employeeCommand.delete(employeeData);
            }
        } catch (JsonProcessingException ignore) {
            log.error("Failed to parse employee record : {}", employeeChanges);
        }
    }

}
