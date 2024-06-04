package com.kihong.pubmodule.adapter.out.persistence.mongo;

import com.kihong.pubmodule.application.port.EmployeeQuery;
import com.kihong.pubmodule.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeReadRepository implements EmployeeQuery {

    private final EmployeeMongoRepository employeeMongoRepository;

    @Override
    public List<Employee> findByName(String name) {

        return employeeMongoRepository.findByName(name);

    }
}
