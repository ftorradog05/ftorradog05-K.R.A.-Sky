package com.krasky.krasky.repository;

import com.krasky.krasky.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    Optional<Vuelo> findByNumeroVuelo(String numeroVuelo);
    List<Vuelo> findByOrigenAndDestino(String origen, String destino);

    // Para saber qué vuelos está usando un avión
    List<Vuelo> findByAvionId(Long avionId);

    // Consulta personalizada para buscar vuelos disponibles (Programados y fecha futura)
    @Query("SELECT v FROM Vuelo v WHERE v.estado = 'PROGRAMADO'")
    List<Vuelo> buscarVuelosDisponibles(@Param("ahora") LocalDateTime ahora);
}