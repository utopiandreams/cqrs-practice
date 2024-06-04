package com.kihong.pubmodule.application.port;

import com.kihong.pubmodule.domain.Employee;

public interface EmployeeCommand {

    void save(Employee employee);

}
