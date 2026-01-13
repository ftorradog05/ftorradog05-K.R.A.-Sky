package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.VueloDTO;
import com.krasky.krasky.model.Avion;
import com.krasky.krasky.model.Vuelo;
import com.krasky.krasky.repository.AvionRepository;
import com.krasky.krasky.repository.VueloRepository;
import com.krasky.krasky.service.VueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VueloServiceImpl implements VueloService {

    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private AvionRepository avionRepository;

    // Entity -> DTO
    private VueloDTO convertToDTO(Vuelo vuelo) {
        VueloDTO dto = new VueloDTO();
        dto.setId(vuelo.getId());
        dto.setNumeroVuelo(vuelo.getNumeroVuelo());
        dto.setOrigen(vuelo.getOrigen());
        dto.setDestino(vuelo.getDestino());
        dto.setFechaSalida(vuelo.getFechaSalida());
        dto.setFechaLlegada(vuelo.getFechaLlegada());
        dto.setPrecioTurista(vuelo.getPrecioTurista());
        dto.setPrecioBusiness(vuelo.getPrecioBusiness());
        dto.setEstado(vuelo.getEstado());

        if (vuelo.getAvion() != null) {
            dto.setAvionId(vuelo.getAvion().getId());
        }
        return dto;
    }

    // DTO -> Entity
    private Vuelo convertToEntity(VueloDTO dto) {
        Vuelo vuelo = new Vuelo();
        vuelo.setId(dto.getId());
        vuelo.setNumeroVuelo(dto.getNumeroVuelo());
        vuelo.setOrigen(dto.getOrigen());
        vuelo.setDestino(dto.getDestino());
        vuelo.setFechaSalida(dto.getFechaSalida());
        vuelo.setFechaLlegada(dto.getFechaLlegada());
        vuelo.setPrecioTurista(dto.getPrecioTurista());
        vuelo.setPrecioBusiness(dto.getPrecioBusiness());
        vuelo.setEstado(dto.getEstado());

        // Buscamos el avión en la BD y lo asignamos
        if (dto.getAvionId() != null) {
            Avion avion = avionRepository.findById(dto.getAvionId()).orElse(null);
            vuelo.setAvion(avion);
        }
        return vuelo;
    }

    @Override
    public List<VueloDTO> getAllVuelos() {
        return vueloRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VueloDTO> getVueloById(Long id) {
        return vueloRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public VueloDTO saveVuelo(VueloDTO vueloDTO) {
        Vuelo vuelo = convertToEntity(vueloDTO);
        Vuelo saved = vueloRepository.save(vuelo);
        return convertToDTO(saved);
    }

    @Override
    public void deleteVuelo(Long id) {
        vueloRepository.deleteById(id);
    }
}