package com.saproject.bancosa.model;

import com.saproject.bancosa.service.ViaCepService;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @Min(value = 0, message = "Idade deve ser um número positivo.")
    private int idade;

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
    private boolean ativo = true;

}
