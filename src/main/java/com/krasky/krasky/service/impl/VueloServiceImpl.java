package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.VueloDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.exception.ResourceNotFoundException;
import com.krasky.krasky.model.Avion;
import com.krasky.krasky.model.EstadoVuelo; // Importación necesaria
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

        // CORRECCIÓN 1: Convertir Enum a String
        if (vuelo.getEstado() != null) {
            dto.setEstado(vuelo.getEstado().name());
        }

        if (vuelo.getAvion() != null) {
            dto.setAvionId(vuelo.getAvion().getId());
            dto.setAvionModelo(vuelo.getAvion().getModelo()); // Extra visual
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

        // CORRECCIÓN 2: Convertir String a Enum
        try {
            if (dto.getEstado() != null) {
                vuelo.setEstado(EstadoVuelo.valueOf(dto.getEstado()));
            } else {
                vuelo.setEstado(EstadoVuelo.PROGRAMADO); // Valor por defecto al crear
            }
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado de vuelo inválido: " + dto.getEstado());
        }

        // Buscamos el avión en la BD y lo asignamos
        if (dto.getAvionId() != null) {
            Avion avion = avionRepository.findById(dto.getAvionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Avión no encontrado con ID: " + dto.getAvionId()));
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
        // Validación: No duplicar número de vuelo al crear
        if (vueloDTO.getId() == null) {
            if (vueloRepository.findByNumeroVuelo(vueloDTO.getNumeroVuelo()).isPresent()) {
                throw new BusinessException("Ya existe un vuelo con el número " + vueloDTO.getNumeroVuelo());
            }
        }

        // Validación de fechas
        if (vueloDTO.getFechaLlegada() != null && vueloDTO.getFechaSalida() != null) {
            if (vueloDTO.getFechaLlegada().isBefore(vueloDTO.getFechaSalida())) {
                throw new BusinessException("La fecha de llegada no puede ser anterior a la de salida");
            }
        }

        Vuelo vuelo = convertToEntity(vueloDTO);
        Vuelo saved = vueloRepository.save(vuelo);
        return convertToDTO(saved);
    }

    @Override
    public void deleteVuelo(Long id) {
        if (!vueloRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vuelo no encontrado");
        }
        vueloRepository.deleteById(id);
    }

    @Override
    public VueloDTO updateVuelo(Long id, VueloDTO vueloDTO) {
        // 1. Buscar el vuelo existente en la base de datos
        Vuelo vueloExistente = vueloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el vuelo con ID: " + id));

        // 2. Validación de negocio: Si el número de vuelo cambia, verificar que no esté ocupado por otro
        if (!vueloExistente.getNumeroVuelo().equals(vueloDTO.getNumeroVuelo())) {
            if (vueloRepository.findByNumeroVuelo(vueloDTO.getNumeroVuelo()).isPresent()) {
                throw new BusinessException("Ya existe otro vuelo con el número " + vueloDTO.getNumeroVuelo());
            }
        }

        // 3. Validación de fechas
        if (vueloDTO.getFechaLlegada().isBefore(vueloDTO.getFechaSalida())) {
            throw new BusinessException("La fecha de llegada no puede ser anterior a la de salida");
        }

        // 4. Actualizar campos simples
        vueloExistente.setNumeroVuelo(vueloDTO.getNumeroVuelo());
        vueloExistente.setOrigen(vueloDTO.getOrigen());
        vueloExistente.setDestino(vueloDTO.getDestino());
        vueloExistente.setFechaSalida(vueloDTO.getFechaSalida());
        vueloExistente.setFechaLlegada(vueloDTO.getFechaLlegada());
        vueloExistente.setPrecioTurista(vueloDTO.getPrecioTurista());
        vueloExistente.setPrecioBusiness(vueloDTO.getPrecioBusiness());

        // 5. Actualizar Estado (String -> Enum)
        try {
            if (vueloDTO.getEstado() != null) {
                vueloExistente.setEstado(EstadoVuelo.valueOf(vueloDTO.getEstado()));
            }
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado de vuelo inválido: " + vueloDTO.getEstado());
        }

        // 6. Actualizar Avión (si cambió)
        if (vueloDTO.getAvionId() != null) {
            Avion avion = avionRepository.findById(vueloDTO.getAvionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Avión no encontrado con ID: " + vueloDTO.getAvionId()));
            vueloExistente.setAvion(avion);
        }

        // 7. Guardar cambios
        Vuelo vueloActualizado = vueloRepository.save(vueloExistente);
        return convertToDTO(vueloActualizado);
    }

    @Override
    public List<VueloDTO> getVuelosDisponibles() {
        // Usamos LocalDateTime.now() para filtrar solo los futuros
        return vueloRepository.buscarVuelosDisponibles(java.time.LocalDateTime.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VueloDTO> buscarPorOrigenYDestino(String origen, String destino) {
        return vueloRepository.findByOrigenAndDestino(origen, destino)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}