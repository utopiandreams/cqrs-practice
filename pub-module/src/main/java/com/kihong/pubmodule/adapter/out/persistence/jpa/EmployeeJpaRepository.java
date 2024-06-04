package com.kihong.pubmodule.adapter.out.persistence.jpa;

import com.kihong.pubmodule.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {
}
