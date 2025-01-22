package com.saproject.accountbank.service;

import com.saproject.accountbank.dto.AccountDTO;
import com.saproject.accountbank.dto.TransactionDTO;
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
            throw new IllegalArgumentException("The deposit amount cannot be negative");
        }

        Account account = accountRepository.findById(id)
                .filter(Account::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        account.setBalance(account.getBalance() + accountDTO.balance());
        return accountRepository.save(account);
    }

    public Account withdraw(Long id, AccountDTO accountDTO) {
        Account account = accountRepository.findById(id)
                .filter(Account::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (account.getBalance() < accountDTO.balance()) {
            throw new IllegalArgumentException("Amount not enough");
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

    public void processTransaction(TransactionDTO transactionDTO) {
        Account originAccount = accountRepository.findByAccountNumber(transactionDTO.originAccountNumber())
                .filter(Account::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Origin account not found or inactive"));

        Account destinationAccount = accountRepository.findByAccountNumber(transactionDTO.destinationAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("Destination account not found"));

        if (originAccount.getBalance() < transactionDTO.amount()) {
            throw new IllegalArgumentException("Insufficient funds in origin account");
        }

        originAccount.setBalance(originAccount.getBalance() - transactionDTO.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionDTO.amount());

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
    }
    public void updateAccountBalance(String accountNumber, AccountDTO accountDTO) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.setBalance(accountDTO.balance());
        accountRepository.save(account);
    }
}
