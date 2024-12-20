package com.saproject.accountbank.controllertest;

import com.saproject.accountbank.controller.AccountController;
import com.saproject.accountbank.dto.AccountDTO;
import com.saproject.accountbank.model.Account;
import com.saproject.accountbank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

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
    void testViewAccountSucess() throws Exception {
        when(accountService.searchById(1L)).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(get("/account/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void testAccountNotFound() throws Exception {
        when(accountService.searchById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/account/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDepositSucess() throws Exception {
        AccountDTO accountDTO = new AccountDTO(500.0);
        when(accountService.deposit(accountDTO, 1L)).thenReturn(account);

        mockMvc.perform(post("/account/deposit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 500.0}")) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500.0));
    }


    @Test
    void testDepositBalanceInvalid() throws Exception {
        mockMvc.perform(post("/account/deposit/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": -500.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWithdrawSucess() throws Exception {
        AccountDTO accountDTO = new AccountDTO(200.0);
        when(accountService.withdraw(1L, accountDTO)).thenReturn(account);

        mockMvc.perform(post("/account/withdraw/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800.0));
    }

    @Test
    void testWithdrawEnoughBalance() throws Exception {
        AccountDTO contaDTO = new AccountDTO(2000.0);
        when(accountService.withdraw(1L, contaDTO)).thenThrow(new IllegalArgumentException("Saldo insuficiente"));
        mockMvc.perform(post("/account/withdraw/{id}", 1L)
                        .param("balance", "2000.0"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    void testChangeAccountStatusSucess() throws Exception {
        when(accountService.accountStatus(1L, false)).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(post("/account/status/{id}", 1L)
                        .param("active", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    void testChangeAccountTypeSucess() throws Exception {
        when(accountService.changeAccountType(1L, Account.accountType.SAVINGS)).thenReturn(java.util.Optional.of(account));

        mockMvc.perform(post("/account/type/{id}", 1L)
                        .param("accountType", "SAVINGS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }
}