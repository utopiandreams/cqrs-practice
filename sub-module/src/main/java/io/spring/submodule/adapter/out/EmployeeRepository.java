package io.spring.submodule.adapter.out;

import io.spring.submodule.application.port.MemberCommand;
import io.spring.submodule.domain.EmployeeDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository implements MemberCommand {

    private final EmployeeMongoRepository employeeRepository;

    @Override
    public void sync(EmployeeDocument employeeDocument) {
        employeeRepository.save(employeeDocument);
    }
}
