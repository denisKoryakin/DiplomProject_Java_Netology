package ru.koryakin.diplomproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.service.UserService;

import java.rmi.ServerException;

@RestController
public class NewUserController {

    @Autowired
    UserService userService;

    /**
     * Регистрация нового пользователя
     */
    @PostMapping(path = "/newUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User newUser) throws ServerException {
        return userService.saveNewUser(newUser);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String user() {
        return "Hello user";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String admin() {
        return "Hello admin";
    }
}
