package com.krasky.krasky.service;

import com.krasky.krasky.dto.PasajeroDTO;
import java.util.List;
import java.util.Optional;

public interface PasajeroService {
    List<PasajeroDTO> getAllPasajeros();
    Optional<PasajeroDTO> getPasajeroById(Long id);
    PasajeroDTO savePasajero(PasajeroDTO pasajeroDTO);
    void deletePasajero(Long id);
    Optional<PasajeroDTO> getPasajeroByDni(String dni);
    PasajeroDTO updatePasajero(Long id, PasajeroDTO pasajeroDTO);
}