package ru.koryakin.diplomproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.repository.UserRepository;

import java.rmi.ServerException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<User> saveNewUser(User newUser) throws ServerException {
        String newPassword = passwordEncoder.encode(newUser.getPassword());
        User toSave = new User(newUser.getUserName(), newPassword, newUser.getRoles());
        userRepository.save(toSave);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
