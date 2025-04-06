package com.Employee.Test.TestDemo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfiguration {
    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
//                .withDatabaseName("testdb")
//                .withUsername("testuser")
//                .withPassword("testpass");
//        container.start();
        return container;
    }
}
