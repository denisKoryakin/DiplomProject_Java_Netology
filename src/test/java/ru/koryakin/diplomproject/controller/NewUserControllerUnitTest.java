package ru.koryakin.diplomproject.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.service.UserService;

@SpringJUnitConfig({
        NewUserController.class
})
public class NewUserControllerUnitTest {

    @Autowired
    NewUserController newUserController;

    @MockBean
    UserService userService;

    @Test
    public void newUserTest() throws Exception {
        //arrange
        User testUser = new User();
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Mockito.when(userService.saveNewUser(testUser)).thenReturn(new ResponseEntity<User>(testUser, HttpStatus.CREATED));

        //act
        ResponseEntity<User> responseEntity = newUserController.create(testUser);

        //assert
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals("testUser", responseEntity.getBody().getUserName());
    }
}
