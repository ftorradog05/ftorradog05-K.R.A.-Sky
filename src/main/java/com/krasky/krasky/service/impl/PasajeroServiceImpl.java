package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.PasajeroDTO;
import com.krasky.krasky.model.Pasajero;
import com.krasky.krasky.repository.PasajeroRepository;
import com.krasky.krasky.service.PasajeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasajeroServiceImpl implements PasajeroService {

    @Autowired
    private PasajeroRepository pasajeroRepository;

    // Entity a DTO
    private PasajeroDTO convertToDTO(Pasajero pasajero) {
        PasajeroDTO dto = new PasajeroDTO();
        dto.setId(pasajero.getId());
        dto.setNombre(pasajero.getNombre());
        dto.setApellidos(pasajero.getApellidos());
        dto.setDni(pasajero.getDni());
        dto.setEmail(pasajero.getEmail());
        dto.setTelefono(pasajero.getTelefono());
        dto.setFechaNacimiento(pasajero.getFechaNacimiento());
        return dto;
    }

    // DTO a Entity
    private Pasajero convertToEntity(PasajeroDTO dto) {
        Pasajero pasajero = new Pasajero();
        pasajero.setId(dto.getId());
        pasajero.setNombre(dto.getNombre());
        pasajero.setApellidos(dto.getApellidos());
        pasajero.setDni(dto.getDni());
        pasajero.setEmail(dto.getEmail());
        pasajero.setTelefono(dto.getTelefono());
        pasajero.setFechaNacimiento(dto.getFechaNacimiento());
        return pasajero;
    }

    @Override
    public List<PasajeroDTO> getAllPasajeros() {
        return pasajeroRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PasajeroDTO> getPasajeroById(Long id) {
        return pasajeroRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public PasajeroDTO savePasajero(PasajeroDTO pasajeroDTO) {
        Pasajero pasajero = convertToEntity(pasajeroDTO);
        Pasajero saved = pasajeroRepository.save(pasajero);
        return convertToDTO(saved);
    }

    @Override
    public void deletePasajero(Long id) {
        pasajeroRepository.deleteById(id);
    }
}