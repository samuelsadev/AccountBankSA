package com.saproject.bancosa.service;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;

    public Optional<Conta> depositar(Long id, double valor) {
        if (valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de depósito deve ser positivo.");
        }

        return contaRepository.findById(id)
                .filter(Conta::podeRealizarOperacao)
                .map(conta -> {
                    conta.setSaldo(conta.getSaldo() + valor);
                    return contaRepository.save(conta);
                });
    }

    public Optional<Conta> sacar(Long id, double valor) {
        return contaRepository.findById(id)
                .filter(Conta::podeRealizarOperacao)
                .map(conta -> {
                    if (conta.getSaldo() < valor) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente.");
                    }
                    conta.setSaldo(conta.getSaldo() - valor);
                    return contaRepository.save(conta);
                });
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
