package com.kihong.pubmodule.application.port;

import com.kihong.pubmodule.domain.Employee;

import java.util.Optional;

public interface EmployeeQuery {

    Optional<Employee> findById(Long id);
}
