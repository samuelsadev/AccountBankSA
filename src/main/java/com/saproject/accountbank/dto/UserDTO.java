package com.saproject.accountbank.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(

        @NotBlank(message = "The email or CPF is mandatory")
        String emailOrCpf,

        @NotBlank(message = "The password is mandatory")
        String password
) {
}
