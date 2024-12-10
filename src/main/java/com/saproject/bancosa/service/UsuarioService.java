package com.saproject.bancosa.service;

import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.dto.UsuarioDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.model.Usuario;
import com.saproject.bancosa.repository.UsuarioRepository;
import com.saproject.bancosa.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ViaCepService viaCepService;

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario criarUsuarioComConta(Usuario usuario, Conta.TipoConta tipoConta) {

        if (usuario.getCep() != null && !usuario.getCep().isEmpty()) {
            ViaCepService.Endereco endereco = viaCepService.buscarEnderecoPorCep(usuario.getCep());
            if (endereco != null) {
                usuario.setEndereco(endereco.getEnderecoCompleto());
            }
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario novoUsuario = usuarioRepository.save(usuario);

        Conta conta = new Conta();
        conta.setUsuario(novoUsuario);
        conta.setTipoConta(tipoConta);
        conta.setNumeroConta(gerarNumeroContaUnico());
        conta.setSaldo(0.0);
        conta.setAtivo(true);

        contaRepository.save(conta);

        return novoUsuario;
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setEmail(usuarioAtualizado.getEmail());

                    if (!usuarioAtualizado.getSenha().equals(usuario.getSenha())) {
                        usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }


    public LoginResponseDTO autenticarUsuario(UsuarioDTO usuarioDTO) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(usuarioDTO.emailOuCpf());

        if (!usuarioOptional.isPresent()) {
            usuarioOptional = usuarioRepository.findByCpf(usuarioDTO.emailOuCpf());
        }

        Usuario usuario = usuarioOptional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email, CPF ou senha inválidos"));

        if (!passwordEncoder.matches(usuarioDTO.senha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email, CPF ou senha inválidos");
        }

        String token = jwtService.generateToken(usuario.getEmail());

        return new LoginResponseDTO(token, usuario);
    }



    private String gerarNumeroContaUnico() {
        return String.format("%08d", System.currentTimeMillis() % 100000000);
    }
}
