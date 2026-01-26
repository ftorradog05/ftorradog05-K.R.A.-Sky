package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.PasajeroDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.exception.ResourceNotFoundException;
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
        return pasajeroRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PasajeroDTO> getPasajeroById(Long id) {
        return pasajeroRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public PasajeroDTO savePasajero(PasajeroDTO pasajeroDTO) {
        // VALIDACIÓN: DNI único
        Optional<Pasajero> porDni = pasajeroRepository.findByDni(pasajeroDTO.getDni());
        if (porDni.isPresent()) {
            // Si estamos creando (id null) o si editamos y el ID no coincide
            if (pasajeroDTO.getId() == null || !porDni.get().getId().equals(pasajeroDTO.getId())) {
                throw new BusinessException("Ya existe un pasajero con el DNI " + pasajeroDTO.getDni());
            }
        }

        // VALIDACIÓN: Email único
        Optional<Pasajero> porEmail = pasajeroRepository.findByEmail(pasajeroDTO.getEmail());
        if (porEmail.isPresent()) {
            if (pasajeroDTO.getId() == null || !porEmail.get().getId().equals(pasajeroDTO.getId())) {
                throw new BusinessException("Ya existe un pasajero con el email " + pasajeroDTO.getEmail());
            }
        }

        Pasajero pasajero = convertToEntity(pasajeroDTO);
        Pasajero saved = pasajeroRepository.save(pasajero);
        return convertToDTO(saved);
    }

    @Override
    public void deletePasajero(Long id) {
        if (!pasajeroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pasajero no encontrado");
        }
        pasajeroRepository.deleteById(id);
    }

    @Override
    public Optional<PasajeroDTO> getPasajeroByDni(String dni) {
        return pasajeroRepository.findByDni(dni)
                .map(this::convertToDTO);
    }

    @Override
    public PasajeroDTO updatePasajero(Long id, PasajeroDTO pasajeroDTO) {
        // 1. Buscar pasajero existente
        Pasajero pasajeroExistente = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + id));

        // 2. Validación DNI único (Si cambió)
        if (!pasajeroExistente.getDni().equals(pasajeroDTO.getDni())) {
            if (pasajeroRepository.findByDni(pasajeroDTO.getDni()).isPresent()) {
                throw new BusinessException("Ya existe otro pasajero con el DNI " + pasajeroDTO.getDni());
            }
        }

        // 3. Validación Email único (Si cambió)
        if (!pasajeroExistente.getEmail().equals(pasajeroDTO.getEmail())) {
            if (pasajeroRepository.findByEmail(pasajeroDTO.getEmail()).isPresent()) {
                throw new BusinessException("Ya existe otro pasajero con el email " + pasajeroDTO.getEmail());
            }
        }

        // 4. Actualizar campos
        pasajeroExistente.setNombre(pasajeroDTO.getNombre());
        pasajeroExistente.setApellidos(pasajeroDTO.getApellidos());
        pasajeroExistente.setDni(pasajeroDTO.getDni());
        pasajeroExistente.setEmail(pasajeroDTO.getEmail());
        pasajeroExistente.setTelefono(pasajeroDTO.getTelefono());
        pasajeroExistente.setFechaNacimiento(pasajeroDTO.getFechaNacimiento());

        // 5. Guardar
        Pasajero actualizado = pasajeroRepository.save(pasajeroExistente);
        return convertToDTO(actualizado);
    }
}