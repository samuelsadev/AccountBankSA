package com.saproject.bancosa.service;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ViaCepService viaCepService;

    public Optional<Conta> cadastrarConta(Conta conta) {
        if (contaRepository.findByCpf(conta.getCpf()).isPresent()) {
            return Optional.empty();
        }

        var endereco = viaCepService.buscarEnderecoPorCep(conta.getCep());
        if (endereco != null) {
            conta.setEndereco(endereco.toString());
        }

        return Optional.of(contaRepository.save(conta));
    }

    public Optional<Conta> atualizarConta(Conta conta) {
        if (contaRepository.findById(conta.getId()).isPresent()) {
            return Optional.of(contaRepository.save(conta));
        }
        return Optional.empty();
    }

    public boolean deletarConta(Long id) {
        return contaRepository.findById(id)
                .map(conta -> {
                    contaRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }
}
