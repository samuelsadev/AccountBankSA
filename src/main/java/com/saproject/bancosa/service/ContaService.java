package com.saproject.bancosa.service;

import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    public Conta depositar(ContaDTO contaDTO, Long id) {

        if (contaDTO.saldo() <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        Conta conta = contaRepository.findById(id)
                .filter(Conta::isAtivo)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada ou inativa."));

        conta.setSaldo(conta.getSaldo() + contaDTO.saldo());
        return contaRepository.save(conta);
    }

    public Conta sacar(Long id, ContaDTO contaDTO) {
        Conta conta = contaRepository.findById(id)
                .filter(Conta::isAtivo)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada ou inativa."));

        if (conta.getSaldo() < contaDTO.saldo()) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        conta.setSaldo(conta.getSaldo() - contaDTO.saldo());
        return contaRepository.save(conta);
    }

    public Optional<Conta> alterarStatus(Long id, boolean ativo) {
        return contaRepository.findById(id)
                .map(conta -> {
                    conta.setAtivo(ativo);
                    return contaRepository.save(conta);
                });
    }

    public Optional<Conta> alterarTipoConta(Long id, Conta.TipoConta tipoConta) {
        return contaRepository.findById(id)
                .map(conta -> {
                    conta.setTipoConta(tipoConta);
                    return contaRepository.save(conta);
                });
    }

    public Optional<Conta> buscarPorId(Long id) {
        return contaRepository.findById(id);
    }

    public Optional<Conta> atualizarConta(Conta conta) {
        return contaRepository.findById(conta.getId())
                .map(existingConta -> {
                    existingConta.setTipoConta(conta.getTipoConta());
                    existingConta.setSaldo(conta.getSaldo());
                    existingConta.setAtivo(conta.isAtivo());
                    return contaRepository.save(existingConta);
                });
    }

    public boolean deletarConta(Long id) {
        return contaRepository.findById(id)
                .map(conta -> {
                    contaRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }
}
