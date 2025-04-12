package com.Employee.Test.TestDemo.advice;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Builder
@Data
public class ApiResponse<T>{
    private T data;
    private HttpStatus status;
    private ApiError error;
}
