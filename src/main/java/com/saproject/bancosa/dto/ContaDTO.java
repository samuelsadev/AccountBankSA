package com.saproject.bancosa.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContaDTO {

    private String emailOuCpf;
    private String senha;
}
