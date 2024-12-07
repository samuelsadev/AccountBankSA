package com.saproject.bancosa.controllertest;

import com.saproject.bancosa.controller.UsuarioController;
import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.dto.UsuarioDTO;
import com.saproject.bancosa.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Adicionado
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioDTO = new UsuarioDTO("email@example.com", "senha");
    }

    @Test
    void testLogin_Sucesso() throws Exception {

        LoginResponseDTO response = new LoginResponseDTO("token", null);
        when(usuarioService.autenticarUsuario(usuarioDTO)).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }
}
