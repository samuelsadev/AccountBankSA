package com.saproject.bancosa.controller;

import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.dto.LoginResponseDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Conta> postConta(@RequestBody @Valid Conta conta) {
        return contaService.cadastrarConta(conta)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid ContaDTO contaDTO) {
        LoginResponseDTO response = contaService.autenticarConta(contaDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> getContaPorId(@PathVariable Long id) {
        return contaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/alterar-status/{id}")
    public ResponseEntity<Conta> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return contaService.alterarStatus(id, ativo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/depositar/{id}")
    public ResponseEntity<Conta> depositar(@PathVariable Long id, @RequestParam double valor) {
        return contaService.depositar(id, valor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/sacar/{id}")
    public ResponseEntity<Conta> sacar(@PathVariable Long id, @RequestParam double valor) {
        return contaService.sacar(id, valor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
