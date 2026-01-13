package com.krasky.krasky.service.impl;

import com.krasky.krasky.dto.ReservaDTO;
import com.krasky.krasky.model.Pasajero;
import com.krasky.krasky.model.Reserva;
import com.krasky.krasky.model.Vuelo;
import com.krasky.krasky.repository.PasajeroRepository;
import com.krasky.krasky.repository.ReservaRepository;
import com.krasky.krasky.repository.VueloRepository;
import com.krasky.krasky.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Entity -> DTO
    private ReservaDTO convertToDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setCodigoReserva(reserva.getCodigoReserva());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setClase(reserva.getClase());
        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setEstado(reserva.getEstado());
        dto.setAsiento(reserva.getAsiento());

        if (reserva.getVuelo() != null) {
            dto.setVueloId(reserva.getVuelo().getId());
        }
        if (reserva.getPasajero() != null) {
            dto.setPasajeroId(reserva.getPasajero().getId());
        }
        return dto;
    }

    // DTO -> Entity
    private Reserva convertToEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setCodigoReserva(dto.getCodigoReserva());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setClase(dto.getClase());
        reserva.setPrecioTotal(dto.getPrecioTotal());
        reserva.setEstado(dto.getEstado());
        reserva.setAsiento(dto.getAsiento());

        // Buscamos Vuelo y Pasajero por ID
        if (dto.getVueloId() != null) {
            Vuelo vuelo = vueloRepository.findById(dto.getVueloId()).orElse(null);
            reserva.setVuelo(vuelo);
        }
        if (dto.getPasajeroId() != null) {
            Pasajero pasajero = pasajeroRepository.findById(dto.getPasajeroId()).orElse(null);
            reserva.setPasajero(pasajero);
        }

        return reserva;
    }

    @Override
    public List<ReservaDTO> getAllReservas() {
        return reservaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservaDTO> getReservaById(Long id) {
        return reservaRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public ReservaDTO saveReserva(ReservaDTO reservaDTO) {
        Reserva reserva = convertToEntity(reservaDTO);
        Reserva saved = reservaRepository.save(reserva);
        return convertToDTO(saved);
    }

    @Override
    public void deleteReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}