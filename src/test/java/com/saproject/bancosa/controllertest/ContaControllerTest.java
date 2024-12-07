package com.saproject.bancosa.controllertest;

import com.saproject.bancosa.controller.ContaController;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    private Conta conta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        conta = new Conta();
        conta.setId(1L);
        conta.setNumeroConta("12345");
        conta.setSaldo(1000.0);
        conta.setAtivo(true);
        conta.setTipoConta(Conta.TipoConta.CORRENTE);
    }

    @Test
    void testVisualizarConta_Sucesso() throws Exception {
        when(contaService.buscarPorId(1L)).thenReturn(java.util.Optional.of(conta));

        mockMvc.perform(get("/contas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroConta").value("12345"))
                .andExpect(jsonPath("$.saldo").value(1000.0));
    }

    @Test
    void testVisualizarConta_ContaNaoEncontrada() throws Exception {
        when(contaService.buscarPorId(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/contas/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDepositar_Sucesso() throws Exception {
        when(contaService.depositar(1L, 500.0)).thenReturn(java.util.Optional.of(conta));

        mockMvc.perform(post("/contas/depositar/{id}", 1L)
                        .param("valor", "500.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(1500.0));
    }

    @Test
    void testDepositar_ValorInvalido() throws Exception {
        mockMvc.perform(post("/contas/depositar/{id}", 1L)
                        .param("valor", "-500.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSacar_Sucesso() throws Exception {
        when(contaService.sacar(1L, 200.0)).thenReturn(java.util.Optional.of(conta));

        mockMvc.perform(post("/contas/sacar/{id}", 1L)
                        .param("valor", "200.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(800.0));
    }

    @Test
    void testSacar_SaldoInsuficiente() throws Exception {
        when(contaService.sacar(1L, 2000.0)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente"));

        mockMvc.perform(post("/contas/sacar/{id}", 1L)
                        .param("valor", "2000.0"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    void testAlterarStatus_Sucesso() throws Exception {
        when(contaService.alterarStatus(1L, false)).thenReturn(java.util.Optional.of(conta));

        mockMvc.perform(post("/contas/alterar-status/{id}", 1L)
                        .param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    void testAlterarTipo_Sucesso() throws Exception {
        when(contaService.alterarTipoConta(1L, Conta.TipoConta.POUPANCA)).thenReturn(java.util.Optional.of(conta));

        mockMvc.perform(post("/contas/alterar-tipo/{id}", 1L)
                        .param("tipoConta", "CONTA_POUPANCA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoConta").value("CONTA_POUPANCA"));
    }
}
