package io.spring.submodule.adapter.out;

import io.spring.submodule.application.port.EmployeeCommand;
import io.spring.submodule.domain.EmployeeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository implements EmployeeCommand {

    private final EmployeeMongoRepository employeeRepository;

    @Override
    public void save(EmployeeDocument employeeDocument) {
        employeeRepository.save(employeeDocument);
    }

    @Override
    public void update(EmployeeDocument employeeDocument) {
        Optional<EmployeeDocument> foundEmployee = employeeRepository.findById(employeeDocument.getId());
        foundEmployee.ifPresent(document -> document.update(employeeDocument));
    }

    @Override
    public void delete(EmployeeDocument employeeDocument) {
        employeeRepository.delete(employeeDocument);
    }
}
