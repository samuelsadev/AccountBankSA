package com.saproject.bancosa.controller;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.model.Conta.TipoConta;
import com.saproject.bancosa.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Endpoint para depositar na conta
    @PostMapping("/depositar/{id}")
    public Conta depositar(@PathVariable Long id, @RequestParam double valor) {
        return contaService.depositar(id, valor).orElseThrow();
    }

    // Endpoint para sacar da conta
    @PostMapping("/sacar/{id}")
    public Conta sacar(@PathVariable Long id, @RequestParam double valor) {
        return contaService.sacar(id, valor).orElseThrow();
    }

    // Endpoint para ativar ou desativar a conta
    @PostMapping("/alterar-status/{id}")
    public Conta alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return contaService.alterarStatus(id, ativo).orElseThrow();
    }

    // Endpoint para alterar o tipo de conta (corrente ou poupança)
    @PostMapping("/alterar-tipo/{id}")
    public Conta alterarTipo(@PathVariable Long id, @RequestParam TipoConta tipoConta) {
        return contaService.alterarTipoConta(id, tipoConta).orElseThrow();
    }
}
