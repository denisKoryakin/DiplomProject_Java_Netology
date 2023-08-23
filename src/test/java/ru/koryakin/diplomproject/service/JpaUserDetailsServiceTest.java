package ru.koryakin.diplomproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({
        JpaUserDetailsService.class
})
class JpaUserDetailsServiceTest {

    @Autowired
    JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        //arrange
        User testUser = new User();
        testUser.setUserid(1);
        testUser.setUserName("testUser");
        testUser.setPassword("password");
        testUser.setRoles("USER");
        Mockito.when(userRepository.findByUserName(Mockito.eq("testUser"))).thenReturn(Optional.of(testUser));
        //act
        UserDetails details = jpaUserDetailsService.loadUserByUsername("testUser");
        //assert
        Assertions.assertEquals(details.getUsername(), "testUser");
        Assertions.assertEquals(details.getAuthorities().toString(), "[USER]");
    }
}