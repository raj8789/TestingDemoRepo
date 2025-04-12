package com.Employee.Test.TestDemo.service;

import com.Employee.Test.TestDemo.TestContainerConfiguration;
import com.Employee.Test.TestDemo.dto.EmployeeDTO;
import com.Employee.Test.TestDemo.entity.EmployeeEntity;
import com.Employee.Test.TestDemo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.Employee.Test.TestDemo.exception.ResourceNotFoundException;

//@SpringBootTest  // this will load whole spring application so it will take more time to run test cases;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ye test database create karta hai it will not perform  database operation in actual database
@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

       @Mock
       //@Autowired
       EmployeeRepository employeeRepository;
       @Spy
       ModelMapper modelMapper;
       // @Autowired
       @InjectMocks
       private EmployeeService employeeService;
       private EmployeeEntity mockObject;

       private EmployeeDTO mockemployeeDTO;
       @BeforeEach
       void setUp(){
           Long empId=1l;
           mockObject= EmployeeEntity.builder()
                   .id(empId)
                   .name("raushan")
                   .email("raushan@gmail.com")
                   .salary(10000).build();
           mockemployeeDTO =modelMapper.map(mockObject, EmployeeDTO.class);
       }
       @Test
       void test_getEmployeeById_When_employeeIsPresent_then_returnEmployeeDTO(){
              Long empId=mockObject.getId();
              when(employeeRepository.findById(empId)).thenReturn(Optional.of(mockObject));
              when(employeeRepository.existsById(empId)).thenReturn(true);
             ResponseEntity<EmployeeDTO> response=employeeService.getEmployeeById(1l);
             EmployeeDTO employeeDTO=response.getBody();
             assertThat(mockObject.getId()).isEqualTo(employeeDTO.getId());
             assertThat(mockObject.getEmail()).isEqualTo(employeeDTO.getEmail());
             verify(employeeRepository,atMost(5)).findById(empId);

       }
       @Test
        void test_createEmployeeDTO_when_EmployeeIsNotPresent(){
           // when(modelMapper.map(mockemployeeDTO,any())).thenReturn(mockObject);
            when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(mockObject);
           // when(modelMapper.map(mockObject,any())).thenReturn(mockemployeeDTO);

           ResponseEntity<EmployeeDTO> response=employeeService.createEmployee(mockemployeeDTO);
           EmployeeDTO employeeDTO=response.getBody();

           ArgumentCaptor<EmployeeEntity> captor=ArgumentCaptor.forClass(EmployeeEntity.class);
           verify(employeeRepository).save(captor.capture());

           EmployeeEntity captureEmployee=captor.getValue();

           assertThat(employeeDTO).isNotNull();
           assertThat(mockObject.getId()).isEqualTo(employeeDTO.getId());
           assertThat(mockObject.getEmail()).isEqualTo(employeeDTO.getEmail());
           assertThat(captureEmployee.getEmail()).isEqualTo(mockObject.getEmail());
          // verify(employeeRepository).save(any(EmployeeEntity.class));

       }
    @Test
    void getEmployeeById_WhenEmployeeIsNotPresent_ThenThrowResourceNotFoundException() {
        Long empId = 2L;
        when(employeeRepository.existsById(empId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(empId));

        verify(employeeRepository, never()).findById(empId);
    }

    @Test
    void getAllEmployee_WhenNoEmployeesExist_ThenReturnEmptyList() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<EmployeeDTO>> response = employeeService.getAllEmployee();
        List<EmployeeDTO> employeeDTOList = response.getBody();

        assertThat(employeeDTOList).isNotNull();
        assertThat(employeeDTOList).isEmpty();
        verify(employeeRepository).findAll();
    }

    @Test
    void deleteEmployee_WhenEmployeeIsPresent_ThenReturnDeletedEmployeeDTO() {
        Long empId = mockObject.getId();
        when(employeeRepository.existsById(empId)).thenReturn(true);
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(mockObject));

        ResponseEntity<EmployeeDTO> response = employeeService.deleteEmployee(empId);
        EmployeeDTO employeeDTO = response.getBody();

        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getId()).isEqualTo(mockObject.getId());
        assertThat(employeeDTO.getEmail()).isEqualTo(mockObject.getEmail());
        verify(employeeRepository).findById(empId);
    }

    @Test
    void deleteEmployee_WhenEmployeeIsNotPresent_ThenThrowResourceNotFoundException() {
        Long empId = 3L;
        when(employeeRepository.existsById(empId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(empId));

        verify(employeeRepository, never()).findById(empId);
    }

    @Test
    void getAllEmployeeByEmail_WhenEmployeesWithEmailExist_ThenReturnEmployeeDTOList() {
        String email = mockObject.getEmail();
        when(employeeRepository.findByEmail(email)).thenReturn(List.of(mockObject));

        ResponseEntity<List<EmployeeDTO>> response = employeeService.getAllEmployeeByEmail(email);
        List<EmployeeDTO> employeeDTOList = response.getBody();

        assertThat(employeeDTOList).isNotNull();
        assertThat(employeeDTOList).hasSize(1);
        assertThat(employeeDTOList.get(0).getEmail()).isEqualTo(email);
        verify(employeeRepository).findByEmail(email);
    }

    @Test
    void getAllEmployeeByEmail_WhenNoEmployeesWithEmailExist_ThenReturnEmptyList() {
        String email = "nonexistent@gmail.com";
        when(employeeRepository.findByEmail(email)).thenReturn(List.of());

        ResponseEntity<List<EmployeeDTO>> response = employeeService.getAllEmployeeByEmail(email);
        List<EmployeeDTO> employeeDTOList = response.getBody();

        assertThat(employeeDTOList).isNotNull();
        assertThat(employeeDTOList).isEmpty();
        verify(employeeRepository).findByEmail(email);
    }
    @Test
    void deleteEmployee_WhenEmployeeExists_ThenRemoveEmployeeAndReturnDTO() {
        Long empId = mockObject.getId();
        when(employeeRepository.existsById(empId)).thenReturn(true);
        when(employeeRepository.findById(empId)).thenReturn(Optional.of(mockObject));

        ResponseEntity<EmployeeDTO> response = employeeService.deleteEmployee(empId);
        EmployeeDTO employeeDTO = response.getBody();

        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getId()).isEqualTo(mockObject.getId());
        assertThat(employeeDTO.getEmail()).isEqualTo(mockObject.getEmail());
        verify(employeeRepository).findById(empId);
        verify(employeeRepository).deleteById(empId);
    }

    @Test
    void deleteEmployee_WhenEmployeeDoesNotExist_ThenThrowResourceNotFoundException() {
        Long empId = 4L;
        when(employeeRepository.existsById(empId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(empId));

        verify(employeeRepository, never()).findById(empId);
        verify(employeeRepository, never()).deleteById(empId);
    }
}