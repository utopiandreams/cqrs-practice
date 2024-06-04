package com.kihong.pubmodule.application.port;

import com.kihong.pubmodule.domain.Employee;

import java.util.List;

public interface EmployeeQuery {

    List<Employee> findByName(String name);

}
