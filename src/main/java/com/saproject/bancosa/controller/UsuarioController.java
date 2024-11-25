package com.saproject.bancosa.controller;

import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.model.Usuario;
import com.saproject.bancosa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public Usuario visualizarUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario criarUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }

    @PostMapping("/login")
    public LoginResponseDTO autenticarUsuario(@RequestBody ContaDTO contaDTO) {
        return usuarioService.autenticarUsuario(contaDTO);
    }

    @PutMapping("/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioAtualizado) {
        return usuarioService.atualizarUsuario(id, usuarioAtualizado);
    }
}
