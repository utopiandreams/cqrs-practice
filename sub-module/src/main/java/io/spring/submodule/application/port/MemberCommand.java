package io.spring.submodule.application.port;

import io.spring.submodule.domain.EmployeeDocument;

public interface MemberCommand {

    void sync(EmployeeDocument employeeDocument);

}
