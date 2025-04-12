package com.Employee.Test.TestDemo.advice;

import com.Employee.Test.TestDemo.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceError(ResourceNotFoundException resourceNotFoundException){
        ApiError error=ApiError.builder().message(resourceNotFoundException.getMessage()).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(ApiResponse.builder().error(error).status(HttpStatus.NOT_FOUND).build(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> argumentError(MethodArgumentNotValidException methodArgumentNotValidException){
        List<String> errorList=methodArgumentNotValidException.getBindingResult().getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.toList());
        ApiError error=ApiError.builder().message("Invalid Resource Found").status(HttpStatus.BAD_REQUEST).erroList(errorList).build();
        return new ResponseEntity<>(ApiResponse.builder().error(error).status(HttpStatus.BAD_REQUEST).build(),HttpStatus.BAD_REQUEST);
    }
}
