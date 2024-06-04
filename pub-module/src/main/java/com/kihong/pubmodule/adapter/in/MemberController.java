package com.kihong.pubmodule.adapter.in;

import com.kihong.pubmodule.application.EmployeeService;
import com.kihong.pubmodule.domain.EmployeeCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<String> save(@RequestBody EmployeeCreate employeeCreate) {

        employeeService.create(employeeCreate);

        return ResponseEntity.ok("직원 저장 성공");
    }



}
