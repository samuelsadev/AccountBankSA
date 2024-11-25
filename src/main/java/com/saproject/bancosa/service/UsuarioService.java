package com.saproject.bancosa.service;

import com.saproject.bancosa.model.Usuario;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.UsuarioRepository;
import com.saproject.bancosa.repository.ContaRepository;
import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.dto.ContaDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private ViaCepService viaCepService;


    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        if (usuario.getCep() != null && !usuario.getCep().isEmpty()) {
            ViaCepService.Endereco endereco = viaCepService.buscarEnderecoPorCep(usuario.getCep());
            if (endereco != null) {
                usuario.setEndereco(endereco.getEnderecoCompleto());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço não encontrado para o CEP fornecido.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP não pode ser vazio.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Conta conta = new Conta();
        conta.setUsuario(usuarioSalvo);
        conta.setNumeroConta(gerarNumeroContaUnico());
        conta.setSaldo(0.0);
        conta.setAtivo(true);
        conta.setTipoConta(Conta.TipoConta.CORRENTE);

        contaRepository.save(conta);

        return usuarioSalvo;
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public LoginResponseDTO autenticarUsuario(ContaDTO contaDTO) {
        var credenciais = new UsernamePasswordAuthenticationToken(contaDTO.getEmailOuCpf(), contaDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {
            String emailOuCpf = contaDTO.getEmailOuCpf();
            Usuario usuario = usuarioRepository.findByEmailOrCpf(emailOuCpf, emailOuCpf)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não encontrado."));
            String token = "Bearer " + jwtService.generateToken(usuario.getEmail());
            return new LoginResponseDTO(token, usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciais inválidas.");
        }
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

    @Transactional
    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (usuarioAtualizado.getNome() != null) {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
        }
        if (usuarioAtualizado.getEmail() != null) {
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        }
        if (usuarioAtualizado.getSenha() != null) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }
        if (usuarioAtualizado.getCep() != null) {
            ViaCepService.Endereco endereco = viaCepService.buscarEnderecoPorCep(usuarioAtualizado.getCep());
            if (endereco != null) {
                usuarioExistente.setEndereco(endereco.getEnderecoCompleto());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Endereço não encontrado para o CEP fornecido.");
            }
        }

        if (usuarioAtualizado.getTelefone() != null) {
            usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
        }
        if (usuarioAtualizado.getCpf() != null) {
            usuarioExistente.setCpf(usuarioAtualizado.getCpf());
        }
        if (usuarioAtualizado.getRg() != null) {
            usuarioExistente.setRg(usuarioAtualizado.getRg());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
