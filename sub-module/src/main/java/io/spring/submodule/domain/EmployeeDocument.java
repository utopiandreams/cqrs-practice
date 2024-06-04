package io.spring.submodule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@Builder
@Document(collection = "Employee")
public class EmployeeDocument {

    @Id
    private String id;
    private String name;
    private String department;

}
