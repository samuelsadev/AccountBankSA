package com.saproject.bancosa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final String agencia = "1321";
    private String numeroConta;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    private double saldo = 0.0;

    private boolean ativo = true;

    public boolean podeRealizarOperacao() {
        return ativo && saldo > 0;
    }

    public enum TipoConta {
        CORRENTE, POUPANCA
    }
}
