package com.Employee.Test.TestDemo.Controller;


import com.Employee.Test.TestDemo.TestContainerConfiguration;
import com.Employee.Test.TestDemo.dto.EmployeeDTO;
import com.Employee.Test.TestDemo.entity.EmployeeEntity;
import com.Employee.Test.TestDemo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestContainerConfiguration.class)
@AutoConfigureWebTestClient(timeout = "10000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // if we not set environment to random port, then it will generate exception for webTestClient
public class AbstractControllerTestIntegration {
    @Autowired
     WebTestClient webTestClient;

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ModelMapper modelMapper;

    EmployeeEntity employeeEntity;
    EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
        employeeEntity = EmployeeEntity.builder()
                .email("raj@gmail.com")
                .name("Raj")
                .salary(10000).build();
        employeeDTO = modelMapper.map(employeeEntity, EmployeeDTO.class);
    }
}
