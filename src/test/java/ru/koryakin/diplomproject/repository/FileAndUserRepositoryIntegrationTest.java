package ru.koryakin.diplomproject.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.entity.User;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(initializers = {FileAndUserRepositoryIntegrationTest.Initializer.class})
@Testcontainers
class FileAndUserRepositoryIntegrationTest {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("db.sql");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @BeforeEach
    private void insertData() {
        User testUser = new User();
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        testUser.setUserFileStorages(List.of(
                new FileStorage("file1", 25, testUser),
                new FileStorage("file2", 20, testUser),
                new FileStorage("file3", 400, testUser),
                new FileStorage("file4", 1, testUser)
        ));
        userRepository.save(testUser);
    }

    @AfterEach
    private void deleteData() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void findAllByTestFileRepository() {
        final var files = fileRepository.findAllBy();
        Assertions.assertEquals(4, files.size());
    }

    @Test
    @Transactional
    void findByFileNameTestFileRepository() {
        Optional<FileStorage> lookingForFile = fileRepository.findByFileName("file1");
        Assertions.assertEquals("file1", lookingForFile.get().getFileName());
        Assertions.assertEquals(25, lookingForFile.get().getSize());
        Assertions.assertEquals("testUser", lookingForFile.get().getUser().getUserName());
    }

    @Test
    @Transactional
    void existsByFileNameTestFileRepository() {
        Assertions.assertTrue(fileRepository.existsByFileName("file1"));
    }

    @Test
    @Transactional
    void findByUserNameTestUserRepository() {
        Optional<User> lookingForUser = userRepository.findByUserName("testUser");
        Assertions.assertEquals("password", lookingForUser.get().getPassword());
        Assertions.assertEquals("USER", lookingForUser.get().getRoles());
    }
}