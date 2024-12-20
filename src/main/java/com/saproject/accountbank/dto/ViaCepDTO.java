package com.saproject.accountbank.dto;

public record ViaCepDTO(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {

    public String getEnderecoCompleto() {
        return logradouro + ", " + bairro + ", " + localidade + " - " + uf;
    }
}
