package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.AvionDTO;
import com.krasky.krasky.model.Avion;
import com.krasky.krasky.repository.AvionRepository;
import com.krasky.krasky.service.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvionServiceImpl implements AvionService {

    @Autowired
    private AvionRepository avionRepository;

    // Convertir de Entidad a DTO
    private AvionDTO convertToDTO(Avion avion) {
        AvionDTO dto = new AvionDTO();
        dto.setId(avion.getId());
        dto.setMatricula(avion.getMatricula());
        dto.setModelo(avion.getModelo());
        dto.setCapacidadTurista(avion.getCapacidadTurista());
        dto.setCapacidadBusiness(avion.getCapacidadBusiness());
        return dto;
    }

    // Convertir de DTO a Entidad
    private Avion convertToEntity(AvionDTO dto) {
        Avion avion = new Avion();
        avion.setId(dto.getId());
        avion.setMatricula(dto.getMatricula());
        avion.setModelo(dto.getModelo());
        avion.setCapacidadTurista(dto.getCapacidadTurista());
        avion.setCapacidadBusiness(dto.getCapacidadBusiness());
        return avion;
    }

    @Override
    public List<AvionDTO> getAllAviones() {
        return avionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AvionDTO> getAvionById(Long id) {
        return avionRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public AvionDTO saveAvion(AvionDTO avionDTO) {
        Avion avion = convertToEntity(avionDTO);
        Avion savedAvion = avionRepository.save(avion);
        return convertToDTO(savedAvion);
    }

    @Override
    public void deleteAvion(Long id) {
        avionRepository.deleteById(id);
    }
}