package com.kihong.pubmodule.adapter.out.persistence.mongo;

import com.kihong.pubmodule.application.port.EmployeeQuery;
import com.kihong.pubmodule.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeReadRepository implements EmployeeQuery {

    private final EmployeeMongoRepository employeeMongoRepository;


    @Override
    public Optional<Employee> findById(Long id) {
        return employeeMongoRepository.findById(id.toString());
    }
}
