package com.saproject.bancosa.repository;

import com.saproject.bancosa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailOrCpf(String email, String cpf);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);
}