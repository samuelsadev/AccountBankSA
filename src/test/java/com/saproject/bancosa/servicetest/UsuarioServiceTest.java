package com.saproject.bancosa.servicetest;

import com.saproject.bancosa.model.Usuario;
import com.saproject.bancosa.repository.UsuarioRepository;
import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.dto.UsuarioDTO;
import com.saproject.bancosa.service.JwtService;
import com.saproject.bancosa.service.UsuarioService;
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
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("email@example.com");
        usuario.setSenha("senha");
        usuarioRepository.save(usuario);

        usuarioDTO = new UsuarioDTO("email@example.com", "senha");
    }

    @Test
    void testAutenticarUsuario_Sucesso() {

        when(passwordEncoder.matches(usuarioDTO.senha(), usuario.getSenha())).thenReturn(true);
        when(jwtService.generateToken(usuario.getEmail())).thenReturn("token");

        LoginResponseDTO response = usuarioService.autenticarUsuario(usuarioDTO);

        assertNotNull(response);
        assertEquals("token", response.token());
    }
}
