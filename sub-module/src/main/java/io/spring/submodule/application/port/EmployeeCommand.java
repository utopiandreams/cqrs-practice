package io.spring.submodule.application.port;

import io.spring.submodule.domain.EmployeeDocument;

public interface EmployeeCommand {

    void save(EmployeeDocument employeeDocument);
    void update(EmployeeDocument employeeDocument);
    void delete(EmployeeDocument employeeDocument);

}
