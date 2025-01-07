package com.saproject.accountbank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    private String name;

    @Min(value = 0, message = "Idade deve ser um número positivo.")
    private int age;

    @Schema(example = "email@email.com.br")
    @Email(message = "Email deve ser válido.")
    private String email;

    @NotBlank(message = "Senha é obrigatória.")
    private String password;

    @NotBlank(message = "CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "RG é obrigatório.")
    private String rg;

    @NotBlank(message = "Telefone é obrigatório.")
    private String number;

    @NotBlank(message = "CEP é obrigatório.")
    private String cep;

    private String address;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("user")
    private Account account;

}
