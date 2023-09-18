package ru.koryakin.diplomproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.service.UserService;

import java.rmi.ServerException;

/**
 * класс регистрации нового пользователя
 */

@RestController
public class NewUserController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/newUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User newUser) {
        return new ResponseEntity<>(userService.saveNewUser(newUser), HttpStatus.CREATED);
    }
}
