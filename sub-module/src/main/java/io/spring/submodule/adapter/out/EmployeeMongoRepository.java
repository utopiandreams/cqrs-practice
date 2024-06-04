package io.spring.submodule.adapter.out;

import io.spring.submodule.domain.EmployeeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeMongoRepository extends MongoRepository<EmployeeDocument, String> {
}
