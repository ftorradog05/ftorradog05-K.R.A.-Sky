package com.krasky.krasky.repository;

import com.krasky.krasky.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    // Aquí podrías buscar reservas por código:
    // Optional<Reserva> findByCodigoReserva(String codigoReserva);
}