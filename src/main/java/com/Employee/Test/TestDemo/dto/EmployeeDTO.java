package com.Employee.Test.TestDemo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    private Long id;

    private String name;
    private String email;
    private Integer salary;
}
