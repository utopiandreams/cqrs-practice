package com.kihong.pubmodule.application;

import com.kihong.pubmodule.application.port.EmployeeCommand;
import com.kihong.pubmodule.application.port.EmployeeQuery;
import com.kihong.pubmodule.domain.Employee;
import com.kihong.pubmodule.domain.EmployeeCreate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeCommand employeeCommand;
    private final EmployeeQuery employeeQuery;

    @Transactional
    public Long create(EmployeeCreate employeeCreate) {
        Employee newEmployee = Employee.create(employeeCreate);
        return employeeCommand.save(newEmployee);
    }

    public Employee getById(Long id) {
        return employeeQuery.findById(id).orElseThrow(() -> new RuntimeException("찾기 실패"));
    }


}
