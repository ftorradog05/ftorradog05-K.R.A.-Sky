package com.krasky.krasky.service;

import com.krasky.krasky.dto.AvionDTO;
import java.util.List;
import java.util.Optional;

public interface AvionService {
    List<AvionDTO> getAllAviones();
    Optional<AvionDTO> getAvionById(Long id);
    AvionDTO saveAvion(AvionDTO avionDTO);
    void deleteAvion(Long id);
}