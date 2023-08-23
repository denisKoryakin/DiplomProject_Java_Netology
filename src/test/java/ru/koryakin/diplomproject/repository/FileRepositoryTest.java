package ru.koryakin.diplomproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.koryakin.diplomproject.config.TestConfig;
import ru.koryakin.diplomproject.entity.FileStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
//@JdbcTest
@DataJpaTest
//@ActiveProfiles("test")
@SpringJUnitConfig({
        FileRepository.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableAutoConfiguration
//@SpringBootTest(classes = {TestConfig.class})
class FileRepositoryTest extends ProlongationTestBase {

    @Autowired
    FileRepository fileRepository;

    @Test
    void findAllBy() {
        final var files = fileRepository.findAllBy();
    }

    @Test
    void findByFileName() {
    }

    @Test
    void existsByFileName() {
    }
}