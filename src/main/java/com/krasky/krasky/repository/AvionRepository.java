package com.krasky.krasky.repository;

import com.krasky.krasky.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Long> {
    Optional<Avion> findByMatricula(String matricula);
}