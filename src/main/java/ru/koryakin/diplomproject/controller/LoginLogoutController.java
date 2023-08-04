package ru.koryakin.diplomproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.koryakin.diplomproject.controller.model.LoginRequest;
import ru.koryakin.diplomproject.controller.model.Token;
import ru.koryakin.diplomproject.service.JpaUserDetailsService;
import ru.koryakin.diplomproject.service.TokenService;

@RestController
public class LoginLogoutController {

    JpaUserDetailsService jpaUserDetailsService;
    // TODO: 03.08.2023 может и не нужен детэйлсервис
    AuthenticationManager authenticationManager;
    TokenService tokenService;

    public LoginLogoutController(JpaUserDetailsService jpaUserDetailsService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
            String token = tokenService.generateToken(authentication);
            return ResponseEntity.ok(new Token(token));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("BadCredentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
