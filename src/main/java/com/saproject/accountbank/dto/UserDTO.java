package com.saproject.accountbank.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(

        @NotBlank(message = "O campo emailOrCpf é obrigatório")
        String emailOrCpf,

        @NotBlank(message = "O campo password é obrigatório")
        String password
) {
}
