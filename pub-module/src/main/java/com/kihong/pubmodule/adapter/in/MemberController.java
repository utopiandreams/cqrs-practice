package com.kihong.pubmodule.adapter.in;

import com.kihong.pubmodule.application.EmployeeService;
import com.kihong.pubmodule.domain.Employee;
import com.kihong.pubmodule.domain.EmployeeCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<String> save(@RequestBody EmployeeCreate employeeCreate) {

        Long id = employeeService.create(employeeCreate);

        return ResponseEntity.ok(id.toString());
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }


}
