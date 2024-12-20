package com.saproject.accountbank.servicetest;

import com.saproject.accountbank.model.User;
import com.saproject.accountbank.repository.UserRepository;
import com.saproject.accountbank.dto.LoginResponseDTO;
import com.saproject.accountbank.dto.UserDTO;
import com.saproject.accountbank.service.JwtService;
import com.saproject.accountbank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsuarioServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("email@example.com");
        user.setPassword("password");
        userRepository.save(user);

        userDTO = new UserDTO("email@example.com", "password");
    }

    @Test
    void testAuthenticateUserSucess() {

        when(passwordEncoder.matches(userDTO.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("token");

        LoginResponseDTO response = userService.authenticateUser(userDTO);

        assertNotNull(response);
        assertEquals("token", response.token());
    }
}
