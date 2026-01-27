package com.krasky.krasky.repository;

import com.krasky.krasky.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByCodigoReserva(String codigoReserva);
    List<Reserva> findByVueloId(Long vueloId);
    List<Reserva> findByPasajeroId(Long pasajeroId);

    // IMPORTANTE: Esta consulta cuenta cuántos asientos "ocupados" (confirmados) tiene un vuelo
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.vuelo.id = :vueloId AND r.estado = 'CONFIRMADA'")
    Long contarReservasConfirmadas(@Param("vueloId") Long vueloId);
}