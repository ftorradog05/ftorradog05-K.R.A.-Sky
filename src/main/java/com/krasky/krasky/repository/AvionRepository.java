package com.krasky.krasky.repository;

import com.krasky.krasky.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Long> {
}