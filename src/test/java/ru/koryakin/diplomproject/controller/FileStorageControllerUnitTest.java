package ru.koryakin.diplomproject.controller;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.koryakin.diplomproject.controller.FilesStorageController;
import ru.koryakin.diplomproject.service.FilesStorageService;

import java.security.Principal;

//@SpringBootTest
//@AutoConfigureMockMvc
public class FileStorageControllerUnitTest {

//    @Autowired
//    private FilesStorageController controller;
//
//    @Test
//    void contextLoads() throws Exception {
//        Assertions.assertNotNull(controller);
//    }

//    @MockBean
//    private FilesStorageController controller;
//
//    @Test
//    void getAllFilesTestUnauthenticated() {
//        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class,
//                () -> controller.getAllFiles("3", null)
//                );
//    }
//
//    @Test
//    @WithMockUser(username = "Frodo", password = "Baggins", roles = "ADMIN")
//    void getAllFilesTest() {
//        controller.getAllFiles("3", (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//    }
}
