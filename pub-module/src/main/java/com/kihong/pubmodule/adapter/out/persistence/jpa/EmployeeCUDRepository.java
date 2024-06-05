package com.kihong.pubmodule.adapter.out.persistence.jpa;

import com.kihong.pubmodule.application.port.EmployeeCommand;
import com.kihong.pubmodule.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeCUDRepository implements EmployeeCommand {

    private final EmployeeJpaRepository employeeJpaRepository;

    @Override
    public Long save(Employee employee) {
        Employee saved = employeeJpaRepository.save(employee);
        employeeJpaRepository.flush();
        return saved.getId();
    }

}
