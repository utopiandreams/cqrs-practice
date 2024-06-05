package com.kihong.pubmodule.application.port;

import com.kihong.pubmodule.domain.Employee;

public interface EmployeeCommand {

    Long save(Employee employee);

}
