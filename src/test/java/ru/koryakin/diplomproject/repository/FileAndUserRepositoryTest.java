package ru.koryakin.diplomproject.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.assertj.AssertableReactiveWebApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.entity.User;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
class FileAndUserRepositoryTest {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

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
    void findAllByTestFileRepository() {
        final var files = fileRepository.findAllBy();
        Assertions.assertEquals(4, files.size());
    }

    @Test
    void findByFileNameTestFileRepository() {
        Optional<FileStorage> lookingForFile = fileRepository.findByFileName("file1");
        Assertions.assertEquals("file1", lookingForFile.get().getFileName());
        Assertions.assertEquals(25, lookingForFile.get().getSize());
        Assertions.assertEquals("testUser", lookingForFile.get().getUser().getUserName());
    }

    @Test
    void existsByFileNameTestFileRepository() {
        Assertions.assertTrue(fileRepository.existsByFileName("file1"));
    }

    @Test
    void findByUserNameTestUserRepository() {
        Optional<User> lookingForUser = userRepository.findByUserName("testUser");
        Assertions.assertEquals("password", lookingForUser.get().getPassword());
        Assertions.assertEquals("USER", lookingForUser.get().getRoles());
    }
}