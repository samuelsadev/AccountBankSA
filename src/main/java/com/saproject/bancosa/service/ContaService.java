package com.saproject.bancosa.service;

import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ViaCepService viaCepService;

    public Optional<Conta> cadastrarConta(Conta conta) {
        if (contaRepository.findByEmailOrCpf(conta.getEmail(), conta.getCpf()).isPresent()) {
            return Optional.empty();
        }
        var endereco = viaCepService.buscarEnderecoPorCep(conta.getCep());
        if (endereco != null) {
            conta.setEndereco(endereco.toString());
        }
        conta.setSenha(criptografarSenha(conta.getSenha()));
        conta.setNumeroConta(gerarNumeroContaUnico());
        return Optional.of(contaRepository.save(conta));
    }

    private String gerarNumeroContaUnico() {
        String numeroConta;
        do {
            numeroConta = gerarNumeroConta();
        } while (contaRepository.existsByNumeroConta(numeroConta));
        return numeroConta;
    }

    private String gerarNumeroConta() {
        int numero = (int) (Math.random() * 1000000);
        int digito = (int) (Math.random() * 10);
        return String.format("%06d-%d", numero, digito);
    }

    public LoginResponseDTO autenticarConta(ContaDTO contaDTO) {
        var credenciais = new UsernamePasswordAuthenticationToken(contaDTO.getEmailOuCpf(), contaDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {
            Conta conta = contaRepository.findByEmailOrCpf(contaDTO.getEmailOuCpf(), contaDTO.getEmailOuCpf())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta não encontrada."));

            String token = "Bearer " + jwtService.generateToken(conta.getEmail());
            return new LoginResponseDTO(token, conta);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciais inválidas.");
        }
    }

    public Optional<Conta> realizarLogin(String cpfOuEmail, String senha) {
        return contaRepository.findByEmailOrCpf(cpfOuEmail, cpfOuEmail)
                .filter(conta -> passwordEncoder.matches(senha, conta.getSenha()));
    }

    private String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public Optional<Conta> findById(Long id) {
        return contaRepository.findById(id);
    }

    public Optional<Conta> atualizarConta(Conta conta) {
        return contaRepository.findById(conta.getId())
                .map(existingConta -> {
                    existingConta.setNome(conta.getNome());
                    existingConta.setEmail(conta.getEmail());
                    existingConta.setTelefone(conta.getTelefone());
                    existingConta.setCep(conta.getCep());
                    existingConta.setIdade(conta.getIdade());
                    existingConta.setSenha(conta.getSenha());
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

    public Optional<Conta> depositar(Long id, double valor) {
        if (valor <= 0) {
            return Optional.empty();
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
                    contaRepository.save(conta);
                    return conta;
                });
    }
}
