package com.saproject.accountbank.dto;

public record TransactionDTO(
        String originAccountNumber,
        String destinationAccountNumber,
        Double amount
) {
}
