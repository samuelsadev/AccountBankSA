package com.saproject.bancosa.controller;

import com.saproject.bancosa.dto.LoginResponseDTO;;
import com.saproject.bancosa.dto.UsuarioDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.model.Usuario;
import com.saproject.bancosa.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Usuario criarUsuario(@Valid @RequestBody Usuario usuario, @RequestParam(required = false) String tipoConta) {
        return usuarioService.criarUsuarioComConta(usuario,
                tipoConta != null ? Conta.TipoConta.valueOf(tipoConta.toUpperCase()) : Conta.TipoConta.CORRENTE);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UsuarioDTO usuarioDTO) {
        LoginResponseDTO response = usuarioService.autenticarUsuario(usuarioDTO);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioAtualizado) {
        return usuarioService.atualizarUsuario(id, usuarioAtualizado);
    }
}
