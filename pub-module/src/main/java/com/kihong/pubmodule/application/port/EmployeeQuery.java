package com.kihong.pubmodule.application.port;

public interface EmployeeQuery {

    Optional<Employee> findByName(String name);

}
