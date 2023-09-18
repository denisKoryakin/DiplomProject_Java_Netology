package ru.koryakin.diplomproject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.koryakin.diplomproject.controller.DTO.Token;
import ru.koryakin.diplomproject.controller.DTO.request.LoginRequest;
import ru.koryakin.diplomproject.service.TokenService;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig({
        LoginLogoutController.class
})
class LoginLogoutControllerUnitTest {

    @Autowired
    LoginLogoutController loginLogoutController;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    TokenService tokenService;

    @BeforeEach
    void requestPreparator() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void login() {
        //arrange
        LoginRequest loginRequest = new LoginRequest("testUser", "password");

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("USER"));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return null;
            }
        };
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password()))).thenReturn(authentication);
        Mockito.when(tokenService.generateToken(authentication)).thenReturn("auth-token");

        //act
        ResponseEntity<?> responseEntity = loginLogoutController.login(loginRequest);
        Token token = (Token) responseEntity.getBody();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assert token != null;
        assertEquals("auth-token", token.getToken());
    }

    @Test
    void logout() {
        //arrange
        //act
        ResponseEntity<?> responseEntity = loginLogoutController.logout();

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}