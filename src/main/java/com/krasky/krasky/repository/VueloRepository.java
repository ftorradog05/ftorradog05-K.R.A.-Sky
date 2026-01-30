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
    List<Vuelo> findByAvionId(Long avionId);

    @Query("SELECT v FROM Vuelo v WHERE v.estado = 'PROGRAMADO' AND v.fechaSalida > :ahora")
    List<Vuelo> buscarVuelosDisponibles(@Param("ahora") LocalDateTime ahora);

    // VUELVE A LA VERSIÓN SOLO ORIGEN Y DESTINO
    @Query("SELECT v FROM Vuelo v WHERE " +
            "(:origen IS NULL OR LOWER(v.origen) LIKE LOWER(CONCAT('%', :origen, '%'))) AND " +
            "(:destino IS NULL OR LOWER(v.destino) LIKE LOWER(CONCAT('%', :destino, '%')))")
    List<Vuelo> buscarVuelos(@Param("origen") String origen, @Param("destino") String destino);
}