package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.AvionDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.exception.ResourceNotFoundException;
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

    private AvionDTO convertToDTO(Avion avion) {
        AvionDTO dto = new AvionDTO();
        dto.setId(avion.getId());
        dto.setMatricula(avion.getMatricula());
        dto.setModelo(avion.getModelo());
        dto.setCapacidadTurista(avion.getCapacidadTurista());
        dto.setCapacidadBusiness(avion.getCapacidadBusiness());
        return dto;
    }

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
        return avionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AvionDTO> getAvionById(Long id) {
        return avionRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public AvionDTO saveAvion(AvionDTO avionDTO) {
        // VALIDACIÓN: Matrícula única
        // Si es nuevo (id null) O si estamos editando y la matrícula cambió
        if (avionDTO.getId() == null) {
            if (avionRepository.findByMatricula(avionDTO.getMatricula()).isPresent()) {
                throw new BusinessException("Ya existe un avión con la matrícula " + avionDTO.getMatricula());
            }
        } else {
            // Caso editar: comprobar si existe otro avión con esa matrícula que no sea este mismo
            Optional<Avion> existente = avionRepository.findByMatricula(avionDTO.getMatricula());
            if (existente.isPresent() && !existente.get().getId().equals(avionDTO.getId())) {
                throw new BusinessException("La matrícula " + avionDTO.getMatricula() + " ya está en uso por otro avión");
            }
        }

        Avion avion = convertToEntity(avionDTO);
        Avion saved = avionRepository.save(avion);
        return convertToDTO(saved);
    }

    @Override
    public void deleteAvion(Long id) {
        if (!avionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: El avión no existe");
        }
        // Aquí podrías validar si el avión tiene vuelos asignados antes de borrar
        avionRepository.deleteById(id);
    }
}