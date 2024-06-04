package com.kihong.pubmodule.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@EntityListeners(EmployeeListener.class)
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String department;

    public static Employee create(EmployeeCreate employeeCreate){
        return Employee.builder()
                .name(employeeCreate.name())
                .department(employeeCreate.department())
                .build();
    }

}
