package com.saproject.accountbank.servicetest;

import com.saproject.accountbank.dto.AccountDTO;
import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.repository.AccountRepository;
import com.saproject.accountbank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContaServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setBalance(1000.0);
        account.setActive(true);
        account.setAccountType(Account.accountType.CURRENT);
    }

    @Test
    void testDepositSucess() {
        AccountDTO contaDTO = new AccountDTO(500.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.deposit(contaDTO, 1L);

        assertNotNull(result);
        assertEquals(1500.0, result.getBalance());
    }

    @Test
    void testDepositBalanceInvalid() {
        AccountDTO contaDTO = new AccountDTO(-500.0);

        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(contaDTO, 1L));
    }
    @Test
    void testWithdrawSucess() {
        AccountDTO accountDTO = new AccountDTO(200.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.withdraw(1L, accountDTO);

        assertNotNull(result);
        assertEquals(800.0, result.getBalance());
    }


    @Test
    void testChangeStatusSucess() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Optional<Account> result = accountService.accountStatus(1L, false);

        assertTrue(result.isPresent());
        assertFalse(result.get().isActive());
    }

    @Test
    void testChangeTipySucess() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Optional<Account> result = accountService.changeAccountType(1L, Account.accountType.SAVINGS);

        assertTrue(result.isPresent());
        assertEquals(Account.accountType.SAVINGS, result.get().getAccountType());
    }
}