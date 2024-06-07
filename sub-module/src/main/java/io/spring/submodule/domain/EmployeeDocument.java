package io.spring.submodule.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Document(collection = "employee")
public class EmployeeDocument {

    @Id
    private String id;
    private String name;
    private String department;

    public void update(EmployeeDocument employee) {
        this.name = employee.getName();
        this.department = employee.getDepartment();
    }

}
