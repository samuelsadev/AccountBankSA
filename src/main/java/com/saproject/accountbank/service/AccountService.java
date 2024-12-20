package com.saproject.accountbank.service;

import com.saproject.accountbank.dto.AccountDTO;
import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account deposit(AccountDTO accountDTO, Long id) {

        if (accountDTO.balance() <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }

        Account account = accountRepository.findById(id)
                .filter(Account::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada ou inativa."));

        account.setBalance(account.getBalance() + accountDTO.balance());
        return accountRepository.save(account);
    }

    public Account withdraw(Long id, AccountDTO accountDTO) {
        Account account = accountRepository.findById(id)
                .filter(Account::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada ou inativa."));

        if (account.getBalance() < accountDTO.balance()) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        account.setBalance(account.getBalance() - accountDTO.balance());
        return accountRepository.save(account);
    }

    public Optional<Account> accountStatus(Long id, boolean active) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setActive(active);
                    return accountRepository.save(account);
                });
    }

    public Optional<Account> changeAccountType(Long id, Account.accountType accountType) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setAccountType(accountType);
                    return accountRepository.save(account);
                });
    }

    public Optional<Account> searchById(Long id) {
        return accountRepository.findById(id);
    }

}
