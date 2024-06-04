package com.kihong.pubmodule.application;

import com.kihong.pubmodule.application.port.EmployeeCommand;
import com.kihong.pubmodule.application.port.EmployeeQuery;
import com.kihong.pubmodule.domain.Employee;
import com.kihong.pubmodule.domain.EmployeeCreate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeCommand employeeCommand;
    private final EmployeeQuery employeeQuery;

    @Transactional
    public void create(EmployeeCreate employeeCreate) {
        Employee newEmployee = Employee.create(employeeCreate);
        employeeCommand.save(newEmployee);
    }

    public List<Employee> find(String name) {
        return employeeQuery.findByName(name);
    }


}
