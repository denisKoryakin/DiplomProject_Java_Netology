package ru.koryakin.diplomproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.repository.UserRepository;

import java.rmi.ServerException;

/**
 * Класс для регистрации нового пользователя
 */

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User saveNewUser(User newUser) {
        String newPassword = passwordEncoder.encode(newUser.getPassword());
        User toSave = new User(newUser.getUserName(), newPassword, newUser.getRoles());
        userRepository.save(toSave);
        log.info("Зарегистрирован новый пользователь: {}, роль: {}", toSave.getUserName(), toSave.getRoles());
        return newUser;
    }
}
