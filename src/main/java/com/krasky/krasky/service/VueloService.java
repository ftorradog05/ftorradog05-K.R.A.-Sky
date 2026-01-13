package com.krasky.krasky.service;

import com.krasky.krasky.dto.VueloDTO;
import java.util.List;
import java.util.Optional;

public interface VueloService {
    List<VueloDTO> getAllVuelos();
    Optional<VueloDTO> getVueloById(Long id);
    VueloDTO saveVuelo(VueloDTO vueloDTO);
    void deleteVuelo(Long id);
}