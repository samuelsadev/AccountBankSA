package com.saproject.bancosa.repository;

import com.saproject.bancosa.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByCpf(String cpf);
}
