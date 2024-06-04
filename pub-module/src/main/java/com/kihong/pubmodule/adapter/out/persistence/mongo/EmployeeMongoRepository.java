package com.kihong.pubmodule.adapter.out.persistence.mongo;


import com.kihong.pubmodule.domain.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeMongoRepository extends MongoRepository<Employee, String> {
    List<Employee> findByName(String name);
}
