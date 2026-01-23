package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.ReservaDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.exception.ResourceNotFoundException;
import com.krasky.krasky.model.*;
import com.krasky.krasky.repository.PasajeroRepository;
import com.krasky.krasky.repository.ReservaRepository;
import com.krasky.krasky.repository.VueloRepository;
import com.krasky.krasky.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private VueloRepository vueloRepository;
    @Autowired
    private PasajeroRepository pasajeroRepository;

    @Override
    @Transactional
    public ReservaDTO saveReserva(ReservaDTO reservaDTO) {

        // 1. Buscar Vuelo y Pasajero
        Vuelo vuelo = vueloRepository.findById(reservaDTO.getVueloId())
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado"));

        Pasajero pasajero = pasajeroRepository.findById(reservaDTO.getPasajeroId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado"));

        // 2. Validar que el vuelo esté PROGRAMADO (Usando el Enum)
        if (vuelo.getEstado() != EstadoVuelo.PROGRAMADO) {
            throw new BusinessException("No se puede reservar en un vuelo que no está PROGRAMADO");
        }

        // 3. Verificar disponibilidad
        Long reservasActuales = reservaRepository.contarReservasConfirmadas(vuelo.getId());

        // Convertimos el String del DTO al Enum para comparar o asignar
        // OJO: Asumimos que el DTO trae "TURISTA" o "BUSINESS" en texto
        int capacidad;
        if ("BUSINESS".equalsIgnoreCase(reservaDTO.getClase())) {
            capacidad = vuelo.getAvion().getCapacidadBusiness();
        } else {
            capacidad = vuelo.getAvion().getCapacidadTurista();
        }

        if (reservasActuales >= capacidad) {
            throw new BusinessException("El vuelo está lleno");
        }

        // 4. Crear/Editar Reserva
        Reserva reserva = new Reserva();

        if (reservaDTO.getId() != null) {
            reserva = reservaRepository.findById(reservaDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        } else {
            reserva.setCodigoReserva("SKY" + System.currentTimeMillis());
            reserva.setFechaReserva(LocalDateTime.now());
            // CORRECCIÓN AQUÍ: Usamos el Enum, no un String
            reserva.setEstado(EstadoReserva.CONFIRMADA);
        }

        reserva.setPasajero(pasajero);
        reserva.setVuelo(vuelo);

        // CORRECCIÓN AQUÍ: Convertimos el String del DTO al Enum ClaseAsiento
        try {
            reserva.setClase(ClaseAsiento.valueOf(reservaDTO.getClase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException("Clase inválida. Debe ser TURISTA o BUSINESS");
        }

        reserva.setAsiento(reservaDTO.getAsiento());

        // 5. Calcular precio
        if (reserva.getClase() == ClaseAsiento.BUSINESS) {
            reserva.setPrecioTotal(vuelo.getPrecioBusiness());
        } else {
            reserva.setPrecioTotal(vuelo.getPrecioTurista());
        }

        Reserva saved = reservaRepository.save(reserva);
        return convertToDTO(saved);
    }

    // --- MÉTODOS AUXILIARES ---

    private ReservaDTO convertToDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCodigoReserva(reserva.getCodigoReserva());
        dto.setFechaReserva(reserva.getFechaReserva());

        // Convertimos Enum a String para el DTO
        if (reserva.getClase() != null) dto.setClase(reserva.getClase().name());
        if (reserva.getEstado() != null) dto.setEstado(reserva.getEstado().name());

        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setAsiento(reserva.getAsiento());

        if (reserva.getVuelo() != null) {
            dto.setVueloId(reserva.getVuelo().getId());
            dto.setVueloNumero(reserva.getVuelo().getNumeroVuelo()); // Info extra
        }
        if (reserva.getPasajero() != null) {
            dto.setPasajeroId(reserva.getPasajero().getId());
            dto.setPasajeroNombre(reserva.getPasajero().getNombre() + " " + reserva.getPasajero().getApellidos()); // Info extra
        }

        return dto;
    }

    @Override
    public List<ReservaDTO> getAllReservas() {
        return reservaRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<ReservaDTO> getReservaById(Long id) {
        return reservaRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public void deleteReserva(Long id) {
        // En lugar de borrar, lo ideal es CANCELAR
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        // CORRECCIÓN: Usar Enum
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    @Override
    public ReservaDTO getReservaByCodigo(String codigo) {
        Reserva reserva = reservaRepository.findByCodigoReserva(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró reserva con código: " + codigo));
        return convertToDTO(reserva);
    }

    @Override
    public List<ReservaDTO> getReservasByPasajeroId(Long pasajeroId) {
        // Primero verificamos si el pasajero existe para dar un error más claro
        if (!pasajeroRepository.existsById(pasajeroId)) {
            throw new ResourceNotFoundException("No existe el pasajero con ID: " + pasajeroId);
        }

        return reservaRepository.findByPasajeroId(pasajeroId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}