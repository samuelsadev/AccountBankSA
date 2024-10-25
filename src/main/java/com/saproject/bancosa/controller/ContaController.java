package com.saproject.bancosa.controller;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.repository.ContaRepository;
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
    ContaService contaService;

    @Autowired
    ContaRepository contaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Conta> getById(@PathVariable long id) {
        return contaRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Conta> postConta(@RequestBody @Valid Conta conta) {
        return contaService.cadastrarConta(conta)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Conta> putConta(@RequestBody @Valid Conta conta) {
        return contaService.atualizarConta(conta)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deleteConta(@PathVariable Long id) {
        return contaService.deletarConta(id)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
