package com.saproject.bancosa.controller;

import com.saproject.bancosa.dto.ContaDTO;
import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.model.Conta.TipoConta;
import com.saproject.bancosa.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @GetMapping("/{id}")
    public Conta visualizarConta(@PathVariable Long id) {
        return contaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    @PostMapping("/depositar/{id}")
    public ResponseEntity<Conta> depositar(@RequestBody ContaDTO contaDTO, @PathVariable Long id) {
        Conta conta = contaService.depositar(contaDTO, id);
        return ResponseEntity.ok(conta);
    }

    @PostMapping("/sacar/{id}")
    public ResponseEntity<Conta> sacar(@RequestBody ContaDTO contaDTO, @PathVariable Long id) {
        Conta conta = contaService.sacar(id, contaDTO);
        return ResponseEntity.ok(conta);
    }


    @PostMapping("/alterar-status/{id}")
    public Conta alterarStatus(@PathVariable Long id, @RequestBody boolean ativo) {
        return contaService.alterarStatus(id, ativo).orElseThrow();
    }

    @PostMapping("/alterar-tipo/{id}")
    public Conta alterarTipo(@PathVariable Long id, @RequestBody TipoConta tipoConta) {
        return contaService.alterarTipoConta(id, tipoConta).orElseThrow();
    }
}
