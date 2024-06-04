package com.kihong.pubmodule.application;

import com.kihong.pubmodule.application.port.EmployeeCommand;
import com.kihong.pubmodule.domain.Employee;
import com.kihong.pubmodule.domain.EmployeeCreate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeCommand employeeCommand;

    @Transactional
    public void create(EmployeeCreate employeeCreate) {
        Employee newEmployee = Employee.create(employeeCreate);
        employeeCommand.save(newEmployee);
    }


}
