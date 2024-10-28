package com.saproject.bancosa.dto;

import com.saproject.bancosa.model.Conta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Conta conta;
}
