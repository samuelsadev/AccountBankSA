package com.saproject.bancosa.servicetest;

import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import com.saproject.bancosa.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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
        ContaDTO contaDTO = new ContaDTO(500.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.depositar(contaDTO, 1L);

        assertNotNull(result);
        assertEquals(1500.0, result.getSaldo());
    }

    @Test
    void testDepositar_ValorInvalido() {
        ContaDTO contaDTO = new ContaDTO(-500.0);

        assertThrows(IllegalArgumentException.class, () -> contaService.depositar(contaDTO, 1L));
    }
    @Test
    void testSacar_Sucesso() {
        ContaDTO contaDTO = new ContaDTO(200.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta result = contaService.sacar(1L, contaDTO);

        assertNotNull(result);
        assertEquals(800.0, result.getSaldo());
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