package com.saproject.bancosa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @Min(value = 0, message = "Idade deve ser um número positivo.")
    private int idade;

    @Schema(example = "email@email.com.br")
    @Email(message = "Email deve ser válido.")
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    private String senha;

    @NotBlank(message = "CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "RG é obrigatório.")
    private String rg;

    @NotBlank(message = "Telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "CEP é obrigatório.")
    private String cep;

    private String endereco;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("usuario")
    private Conta conta;

}
