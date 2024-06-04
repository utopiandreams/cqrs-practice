package io.spring.submodule.application.port;

import io.spring.submodule.domain.EmployeeDocument;

public interface EmployeeCommand {

    void sync(EmployeeDocument employeeDocument);

}
