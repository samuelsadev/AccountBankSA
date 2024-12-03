package com.saproject.bancosa.repository;

import com.saproject.bancosa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailOrCpf(String email, String cpf);

    @Query("SELECT u FROM Usuario u WHERE u.email = :emailOrCpf OR u.cpf = :emailOrCpf")
    Optional<Usuario> findByEmailOrCpf(@Param("emailOrCpf") String emailOrCpf);


    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);
}