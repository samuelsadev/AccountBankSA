package com.saproject.bancosa.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(

        @NotBlank(message = "O campo emailOuCpf é obrigatório")
        String emailOuCpf,

        @NotBlank(message = "O campo senha é obrigatório")
        String senha
) {
}
