package com.saproject.bancosa.dto;

import lombok.Data;

@Data
public class ViaCepDTO {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    public String getEnderecoCompleto() {
        return logradouro + ", " + bairro + ", " + localidade + " - " + uf;
    }
}
