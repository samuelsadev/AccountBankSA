package com.saproject.accountbank.controller;

import com.saproject.accountbank.dto.AccountDTO;
import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.model.Account.accountType;
import com.saproject.accountbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public Account viewAccount(@PathVariable Long id) {
        return accountService.searchById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    @PostMapping("/deposit/{id}")
    public ResponseEntity<Account> deposit(@RequestBody AccountDTO accountDTO, @PathVariable Long id) {
        Account account = accountService.deposit(accountDTO, id);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/withdraw/{id}")
    public ResponseEntity<Account> withdraw(@RequestBody AccountDTO accountDTO, @PathVariable Long id) {
        Account account = accountService.withdraw(id, accountDTO);
        return ResponseEntity.ok(account);
    }


    @PostMapping("/status/{id}")
    public Account accountStatus(@PathVariable Long id, @RequestBody boolean active) {
        return accountService.accountStatus(id, active).orElseThrow();
    }

    @PostMapping("/type/{id}")
    public Account accountType(@PathVariable Long id, @RequestBody accountType accountType) {
        return accountService.changeAccountType(id, accountType).orElseThrow();
    }
}
