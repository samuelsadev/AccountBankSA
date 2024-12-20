package com.saproject.accountbank.repository;

import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);
    Optional<Account> findByUser(User user);
}
