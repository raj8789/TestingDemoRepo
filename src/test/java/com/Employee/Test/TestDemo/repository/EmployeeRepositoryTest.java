package com.Employee.Test.TestDemo.repository;

import com.Employee.Test.TestDemo.TestContainerConfiguration;
import com.Employee.Test.TestDemo.entity.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;

@Import(TestContainerConfiguration.class)
//@SpringBootTest  // this will load whole spring application so it will take more time to run test cases;
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED) // ye test database create karta hai it will not perform  database operation in actual database
@DataJpaTest // it will create empty  test table (production table pe kuch v impact nahi hota hai) and ye sare test method ko run krne ke bad databse se value remove kr dega taki new test method ke lie database empty mile
class EmployeeRepositoryTest {

    @Autowired
    private  EmployeeRepository employeeRepository;
    private EmployeeEntity employeeEntity;

    @BeforeEach
    public void setUp(){
       employeeEntity= EmployeeEntity.builder()
               // .id(1L)
                .name("raushan")
                .email("raushan@gmail.com")
                .salary(20000)
                .build();
    }
    @Test
    void testFindByEmail_whenEmailIsValid_thenReturnEmployee() {
           // arrange , Given
        employeeRepository.save(employeeEntity);
            // Act , when
        List<EmployeeEntity> entityList=employeeRepository.findByEmail(employeeEntity.getEmail());
        // Assert , then
        assertThat(entityList).isNotEmpty();
        assertThat(entityList).isNotNull();
        assertThat(entityList.get(0).getEmail().equals(employeeEntity.getEmail()));
    }
    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmployee() {
        List<EmployeeEntity> entityList=employeeRepository.findByEmail(employeeEntity.getEmail());
        assertThat(entityList).isEmpty();
    }
}