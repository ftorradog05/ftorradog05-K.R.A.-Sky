package com.krasky.krasky.service;

import com.krasky.krasky.dto.ReservaDTO;
import java.util.List;
import java.util.Optional;

public interface ReservaService {
    List<ReservaDTO> getAllReservas();
    Optional<ReservaDTO> getReservaById(Long id);
    ReservaDTO saveReserva(ReservaDTO reservaDTO);
    void deleteReserva(Long id);
}