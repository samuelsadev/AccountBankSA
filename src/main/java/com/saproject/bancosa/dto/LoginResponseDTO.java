package com.saproject.bancosa.dto;

import com.saproject.bancosa.model.Usuario;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private Usuario usuario;

    public LoginResponseDTO(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }
}
