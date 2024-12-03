package com.saproject.bancosa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {

    @NotBlank(message = "O campo emailOuCpf é obrigatório")
    private String emailOuCpf;

    @NotBlank(message = "O campo senha é obrigatório")
    private String senha;
}
