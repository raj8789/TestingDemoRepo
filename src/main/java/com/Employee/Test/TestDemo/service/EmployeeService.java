package com.Employee.Test.TestDemo.service;

import com.Employee.Test.TestDemo.dto.EmployeeDTO;
import com.Employee.Test.TestDemo.entity.EmployeeEntity;
import com.Employee.Test.TestDemo.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository,ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper =modelMapper;
    }

    public ResponseEntity<EmployeeDTO> createEmployee(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity = employeeRepository.save(employeeEntity);
        employeeDTO=modelMapper.map(employeeEntity, EmployeeDTO.class);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    public ResponseEntity<EmployeeDTO> getEmployeeById(Long employeeId) {
        if(employeeRepository.existsById(employeeId)){
            EmployeeEntity employeeEntity=employeeRepository.findById(employeeId).get();
            EmployeeDTO employeeDTO=modelMapper.map(employeeEntity, EmployeeDTO.class);
            return new ResponseEntity<>(employeeDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<EmployeeDTO>> getAllEmployee() {
        List<EmployeeDTO> employeeDTOList=employeeRepository.findAll().stream().map(
                employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class)
        ).collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList,HttpStatus.OK);
    }

    public ResponseEntity<EmployeeDTO> deleteEmployee(Long employeeId) {
        if(employeeRepository.existsById(employeeId)){
            EmployeeEntity employeeEntity=employeeRepository.findById(employeeId).get();
            EmployeeDTO employeeDTO=modelMapper.map(employeeEntity, EmployeeDTO.class);
            return new ResponseEntity<>(employeeDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    public ResponseEntity<List<EmployeeDTO>> getAllEmployeeByEmail(String email) {
        List<EmployeeDTO> employeeDTOList=employeeRepository.findByEmail(email).stream().map(
                employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class)
        ).collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList,HttpStatus.OK);
    }
}
