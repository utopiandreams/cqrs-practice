package io.spring.submodule.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.submodule.application.port.EmployeeCommand;
import io.spring.submodule.domain.EmployeeDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeCommand employeeCommand;
    private final ObjectMapper objectMapper;

    public void save(String employeeRecord) {
        try {
            String data = objectMapper.readTree(employeeRecord).get("data").asText();
            EmployeeDocument employeeDocument = objectMapper.readValue(data, EmployeeDocument.class);
            employeeCommand.sync(employeeDocument);
        } catch (JsonProcessingException ignore) {
            log.error("Failed to parse employee record : {}", employeeRecord);
        }
    }

}
