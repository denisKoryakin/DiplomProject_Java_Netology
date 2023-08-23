package ru.koryakin.diplomproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class FileRepositoryTest {

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