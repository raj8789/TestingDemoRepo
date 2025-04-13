package com.Employee.Test.TestDemo.Controller;


import com.Employee.Test.TestDemo.dto.EmployeeDTO;
import com.Employee.Test.TestDemo.entity.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import java.util.*;

import static org.assertj.core.api.Assertions.*;



class EmployeeControllerTestIT extends AbstractControllerTestIntegration{


    @Test
    void testWhenEmployeeById_isPresent(){
        EmployeeEntity savedEmployee=employeeRepository.save(employeeEntity);
        employeeDTO.setId(savedEmployee.getId());
        webTestClient.get().uri("/emp/find/{employeeId}",savedEmployee.getId())
                .exchange().expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .value(employeeDTO1 ->{
                    assertThat(employeeDTO1).isNotNull();
                    assertThat(employeeDTO1.getName()).isEqualTo(employeeDTO.getName());
                    assertThat(employeeDTO1.getId()).isEqualTo(employeeDTO.getId());
                    assertThat(employeeDTO1.getEmail()).isEqualTo(employeeDTO.getEmail());
                });
    }
    @Test
    void testWhenEmployeeById_isNotPresent(){
            webTestClient.get().uri("/emp/find/1")
                    .exchange()
                    .expectStatus().isNotFound();
    }
    @Test
    void testCreateEmployee_WhenEmployeeAlreadyExists(){
        webTestClient.post().uri("emp/create")
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void testDeleteEmployee_WhenNotPresent(){
        webTestClient.delete().uri("emp/delete/2")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testDeleteEmployee_WhenEmployeeIsPresent(){
       EmployeeEntity saved=employeeRepository.save(employeeEntity);
       employeeEntity.setId(saved.getId());
       employeeDTO.setId(saved.getId());
        webTestClient.delete()
                .uri("emp/delete/{employeeId}",saved.getId())
                .exchange().expectBody(EmployeeDTO.class)
                .value(employeeDTO1 -> {
                   assertThat(employeeDTO1).isNotNull();
                   assertThat(employeeDTO1.getId()).isEqualTo(employeeDTO.getId());
                   assertThat(employeeDTO1.getEmail()).isEqualTo(employeeDTO.getEmail());
                });
    }
    @Test
    void testFindAllEmployeeByEmail_WhenNoEmployeeExists(){
        EmployeeEntity saved=employeeRepository.save(employeeEntity);
        employeeEntity.setId(saved.getId());
        employeeDTO.setId(saved.getId());
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/emp/find/byEmail")
                        .queryParam("email", "pinku@gmail.com")
                        .build()).
                exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<EmployeeDTO>>() {})
                .value(employeeDTOS -> {
                    assertThat(employeeDTOS).isNotNull();
                    assertThat(employeeDTOS).isEmpty();
                });
    }
    @Test
    void testFindAllEmployeeByEmail_WhenEmployeeExists(){
        EmployeeEntity saved=employeeRepository.save(employeeEntity);
        employeeEntity.setId(saved.getId());
        employeeDTO.setId(saved.getId());
        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/emp/find/byEmail")
                        .queryParam("email", employeeDTO.getEmail())
                        .build()).
                exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<EmployeeDTO>>() {})
                .value(employeeDTOS -> {
                    assertThatNoException();
                    assertThat(employeeDTOS.size()).isEqualTo(1);
                });
    }
    @Test
    void testGetAllEmployee_WhenNoEmployeeExists(){
        webTestClient.get().uri("emp/find/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<EmployeeDTO>>() {
                })
                .value(employeeDTOS -> {
                    assertThat(employeeDTOS).isNotNull();
                    assertThat(employeeDTOS).isEmpty();
                });
    }
    @Test
    void testFindAllEmployee_WhenEmployeeDoesExists(){
         employeeRepository.save(employeeEntity);
        webTestClient.get().uri("emp/find/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<EmployeeDTO>>() {
                })
                .value(employeeDTOS -> {
                    assertThat(employeeDTOS).isNotNull();
                    assertThat(employeeDTOS.size()).isEqualTo(1);
                });
    }

}