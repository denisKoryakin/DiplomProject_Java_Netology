package ru.koryakin.diplomproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.exception.FileError;
import ru.koryakin.diplomproject.repository.FileRepository;
import ru.koryakin.diplomproject.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringJUnitConfig({
        JpaUserDetailsService.class,
        FilesStorageService.class
})
public class FileStorageServiceUnitTest {

    @Autowired
    JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    FilesStorageService filesStorageService;

    @MockBean
    FileRepository fileRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    Principal principal;

    @MockBean
    MultipartFile multipartFile;

    @Test
    void getAllFilesTestWithAdmin() {
        //arrange
        User testAdmin = new User();
        testAdmin.setUserid(1);
        testAdmin.setUserName("testAdmin");
        testAdmin.setPassword("password");
        testAdmin.setRoles("ADMIN");

        List<FileStorage> files = List.of(
                new FileStorage("filename1", 10, null),
                new FileStorage("filename2", 2, null),
                new FileStorage("filename3", 30, null),
                new FileStorage("filename4", 90, null)
        );

        Mockito.when(principal.getName()).thenReturn("testAdmin");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testAdmin"))).thenReturn(Optional.of(testAdmin));
        Mockito.when(fileRepository.findAllBy()).thenReturn(files);
        //act
        List<Object> objects = filesStorageService.getAllFiles("3", principal);
        //assert
        Assertions.assertEquals(3, objects.size());
    }

    @Test
    void getAllFilesTestWithUser() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");

        List<FileStorage> files = List.of(
                new FileStorage("filename1", 10, new User()),
                new FileStorage("filename2", 2, testUser),
                new FileStorage("filename3", 30, new User()),
                new FileStorage("filename4", 90, new User())
        );

        Mockito.when(principal.getName()).thenReturn("testUser");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testUser"))).thenReturn(Optional.of(testUser));
        Mockito.when(fileRepository.findAllBy()).thenReturn(files);
        //act
        List<Object> objects = filesStorageService.getAllFiles("3", principal);
        //assert
        Assertions.assertEquals(1, objects.size());
    }

    @Test
    void getAllFilesTestThrowsException() {
        Assertions.assertThrows(FileError.class, () -> filesStorageService.getAllFiles("3", principal));
    }

    @Test
    void uploadFileTestByUser() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        testUser.setUserFileStorages(new ArrayList<>());

        String filename = "filenameTest";

        Mockito.when(principal.getName()).thenReturn("testUser");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testUser"))).thenReturn(Optional.of(testUser));
        Mockito.when(multipartFile.getSize()).thenReturn(15L);
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(fileRepository.existsByFileName(Mockito.eq("filenameTest"))).thenReturn(true);
        Mockito.when(fileRepository.uploadFile("filenameTest", multipartFile, principal.getName())).thenReturn(true);
        //act
        filesStorageService.uploadFile("filenameTest", multipartFile, principal);
        //assert
        Assertions.assertEquals(1, testUser.getUserFileStorages().size());
    }

    @Test
    void deleteFileTestByUser() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        testUser.setUserFileStorages(new ArrayList<>());

        FileStorage testFile = new FileStorage();
        testFile.setFileName("filenameTest");
        testFile.setSize(25);
        testFile.setUser(testUser);

        testUser.getUserFileStorages().add(testFile);

        Mockito.when(principal.getName()).thenReturn("testUser");
        Mockito.when(fileRepository.findByFileName(Mockito.eq("filenameTest"))).thenReturn(Optional.of(testFile));
        Mockito.when(userRepository.findByUserName(Mockito.eq("testUser"))).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(fileRepository.existsByFileName("filenameTest")).thenReturn(false);
        Mockito.when(fileRepository.deleteFile(testFile, testUser.getUserName())).thenReturn(true);
        //act
        filesStorageService.deleteFile("filenameTest", principal);
        //assert
        Assertions.assertEquals(0, testUser.getUserFileStorages().size());
    }

    @Test
    void deleteFileTestByAdmin() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        testUser.setUserFileStorages(new ArrayList<>());

        User testAdmin = new User();
        testAdmin.setUserid(1);
        testAdmin.setUserName("testAdmin");
        testAdmin.setPassword("password");
        testAdmin.setRoles("ADMIN");

        FileStorage testFile = new FileStorage();
        testFile.setFileName("filenameTest");
        testFile.setSize(25);
        testFile.setUser(testUser);

        testUser.getUserFileStorages().add(testFile);

        Mockito.when(fileRepository.findByFileName(Mockito.eq("filenameTest"))).thenReturn(Optional.of(testFile));
        Mockito.when(principal.getName()).thenReturn("testAdmin");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testAdmin"))).thenReturn(Optional.of(testAdmin));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(fileRepository.existsByFileName("filenameTest")).thenReturn(false);
        Mockito.when(fileRepository.deleteFile(testFile, testUser.getUserName())).thenReturn(true);
        //act
        filesStorageService.deleteFile("filenameTest", principal);
        //assert
        Assertions.assertEquals(0, testUser.getUserFileStorages().size());
    }

    @Test
    void editFilenameTestByUser() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        testUser.setUserFileStorages(List.of(
                new FileStorage("filename1", 10, testUser),
                new FileStorage("filename2", 2, testUser),
                new FileStorage("filename3", 30, testUser),
                new FileStorage("filename4", 90, testUser)
        ));

        String filename = "filename1";
        String newFilename = "newFilename";

        Mockito.when(principal.getName()).thenReturn("testUser");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testUser"))).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(fileRepository.existsByFileName(newFilename)).thenReturn(true);
        Mockito.when(fileRepository.renameFile(filename, newFilename, testUser.getUserName())).thenReturn(true);
        //act
        filesStorageService.editFileName(filename, newFilename, principal);
        //assert
        Assertions.assertTrue(testUser.getUserFileStorages().stream().anyMatch(file -> file.getFileName().equals("newFilename")));
        Assertions.assertTrue(testUser.getUserFileStorages().stream().noneMatch(file -> file.getFileName().equals("filename1")));

    }

    @Test
    void getFileByFilenameTestByUser() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");

        String filename = "filename";

        Mockito.when(principal.getName()).thenReturn("testUser");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testUser"))).thenReturn(Optional.of(testUser));
        Mockito.when(fileRepository.getFileByFilename(filename, testUser.getUserName())).thenReturn(new byte[100]);
        //act
        //assert
        Assertions.assertEquals(100, filesStorageService.getFileByFilename(filename, principal).getByteArray().length);
    }
}
