package com.kihong.pubmodule.domain;

import lombok.Builder;

@Builder
public record EmployeeCreate (
        String name,
        String department
) {

}
