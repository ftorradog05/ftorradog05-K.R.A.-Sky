package com.krasky.krasky.repository;

import com.krasky.krasky.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // ESTE ES EL MÉTODO QUE USA EL LOGIN:
    Optional<Usuario> findByUsername(String username);

    // Otros métodos útiles (opcionales)
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}