package com.saproject.bancosa.servicetest;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import com.saproject.bancosa.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

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
    void testDepositar_Sucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Optional<Conta> result = contaService.depositar(1L, 500.0);

        assertTrue(result.isPresent());
        assertEquals(1500.0, result.get().getSaldo());
    }

    @Test
    void testDepositar_ValorInvalido() {
        assertThrows(ResponseStatusException.class, () -> contaService.depositar(1L, -500.0));
    }

    @Test
    void testSacar_Sucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Optional<Conta> result = contaService.sacar(1L, 200.0);

        assertTrue(result.isPresent());
        assertEquals(800.0, result.get().getSaldo());
    }

    @Test
    void testSacar_SaldoInsuficiente() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        assertThrows(ResponseStatusException.class, () -> contaService.sacar(1L, 2000.0));
    }

    @Test
    void testAlterarStatus_Sucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Optional<Conta> result = contaService.alterarStatus(1L, false);

        assertTrue(result.isPresent());
        assertFalse(result.get().isAtivo());
    }

    @Test
    void testAlterarTipoConta_Sucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Optional<Conta> result = contaService.alterarTipoConta(1L, Conta.TipoConta.POUPANCA);

        assertTrue(result.isPresent());
        assertEquals(Conta.TipoConta.POUPANCA, result.get().getTipoConta());
    }
}
