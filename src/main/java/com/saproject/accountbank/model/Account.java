package com.saproject.accountbank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final String agency = "1321";
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private accountType accountType;

    private double balance = 0.0;

    private boolean active = true;

    public boolean canPerformOperation() {
        return active && balance > 0;
    }

    public enum accountType {
        CURRENT, SAVINGS
    }
}
