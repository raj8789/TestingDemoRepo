package com.Employee.Test.TestDemo.Controller;

import com.Employee.Test.TestDemo.dto.EmployeeDTO;
import com.Employee.Test.TestDemo.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO){
        return employeeService.createEmployee(employeeDTO);
    }
    @GetMapping("/find/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long employeeId){
        return employeeService.getEmployeeById(employeeId);
    }
    @GetMapping("/find/all")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployee(){
        return employeeService.getAllEmployee();
    }

    @GetMapping("/find/byEmail")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeeByEmail(@RequestParam String email){
        return employeeService.getAllEmployeeByEmail(email);
    }
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<EmployeeDTO> deleteEmployee(@PathVariable Long employeeId){
            return employeeService.deleteEmployee(employeeId);
    }
}
