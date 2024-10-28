package com.saproject.bancosa.service;

import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
import com.saproject.bancosa.security.JwtService;
import lombok.RequiredArgsConstructor;
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

    public Optional<Conta> cadastrarConta(Conta conta) {
        if (contaRepository.findByEmailOrCpf(conta.getEmail(), conta.getCpf()).isPresent()) {
            return Optional.empty();
        }
        conta.setSenha(criptografarSenha(conta.getSenha()));
        return Optional.of(contaRepository.save(conta));
    }

    public LoginResponseDTO autenticarConta(ContaDTO contaDTO) {
        // Autentica as credenciais fornecidas
        var credenciais = new UsernamePasswordAuthenticationToken(contaDTO.getEmailOuCpf(), contaDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {
            // Busca a conta usando email ou CPF
            Conta conta = contaRepository.findByEmailOrCpf(contaDTO.getEmailOuCpf(), contaDTO.getEmailOuCpf())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta não encontrada."));

            // Gera o token e retorna junto com a informação da conta
            String token = "Bearer " + jwtService.generateToken(conta.getEmail());
            return new LoginResponseDTO(token, conta); // Retorna o token e os detalhes da conta
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciais inválidas.");
        }
    }

    public Optional<Conta> realizarLogin(String cpfOuEmail, String senha) {
        // Busca a conta e compara a senha
        return contaRepository.findByEmailOrCpf(cpfOuEmail, cpfOuEmail)
                .filter(conta -> passwordEncoder.matches(senha, conta.getSenha()));
    }

    private String criptografarSenha(String senha) {
        // Criptografa a senha usando BCrypt
        return passwordEncoder.encode(senha);
    }

    public Optional<Conta> atualizarConta(Conta conta) {
        return contaRepository.findById(conta.getId())
                .map(existingConta -> {
                    existingConta.setNome(conta.getNome());
                    existingConta.setEmail(conta.getEmail());
                    existingConta.setTelefone(conta.getTelefone());
                    existingConta.setCep(conta.getCep());
                    return Optional.of(contaRepository.save(existingConta));
                }).orElse(Optional.empty());
    }

    public boolean deletarConta(Long id) {
        return contaRepository.findById(id)
                .map(conta -> {
                    contaRepository.deleteById(id);
                    return true;
                }).orElse(false);
    }

    public Optional<Conta> depositar(Long id, double valor) {
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
}
