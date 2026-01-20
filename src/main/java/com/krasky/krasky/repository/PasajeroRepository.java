package com.krasky.krasky.repository;

import com.krasky.krasky.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    Optional<Pasajero> findByDni(String dni);
    Optional<Pasajero> findByEmail(String email);

    // Búsqueda flexible por nombre (ignorando mayúsculas/minúsculas)
    @Query("SELECT p FROM Pasajero p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Pasajero> buscarPorNombre(@Param("termino") String termino);
}