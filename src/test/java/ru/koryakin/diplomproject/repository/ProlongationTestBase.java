package ru.koryakin.diplomproject.repository;

import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@TestPropertySource(
    properties = {
        "spring.datasource.url=jdbc:tc:postgresql:12.1:///databasename"
    })
public class ProlongationTestBase {

    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(DockerImageName.parse("postgres:12.7"))
        .withDatabaseName("postgres")
        .withUsername("postgres")
        .withPassword("postgres")
        .withExposedPorts(5432);
}