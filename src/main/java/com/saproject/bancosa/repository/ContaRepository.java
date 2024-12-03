package com.saproject.bancosa.repository;

import com.saproject.bancosa.model.Conta;
import com.saproject.bancosa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    boolean existsByNumeroConta(String numeroConta);
    Optional<Conta> findByUsuario(Usuario usuario);
}
