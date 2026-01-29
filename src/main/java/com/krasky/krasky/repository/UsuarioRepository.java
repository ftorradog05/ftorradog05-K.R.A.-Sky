package com.krasky.krasky.repository;

import com.krasky.krasky.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para buscar usuarios por su nombre de login (necesario para el Login)
    Optional<Usuario> findByUsername(String username);

    // Métodos para comprobar si ya existen (necesarios para el Registro)
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}