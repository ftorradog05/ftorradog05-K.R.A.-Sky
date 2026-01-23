package com.krasky.krasky.controller.rest;

import com.krasky.krasky.dto.ReservaDTO;
import com.krasky.krasky.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaRestController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        return ResponseEntity.ok(reservaService.getAllReservas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable Long id) {
        return reservaService.getReservaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> createReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        return ResponseEntity.ok(reservaService.saveReserva(reservaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ReservaDTO> getReservaByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(reservaService.getReservaByCodigo(codigo));
    }

    // 2. Buscar Reservas de un Pasajero (ej: /api/reservas/pasajero/1)
    @GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<ReservaDTO>> getReservasByPasajero(@PathVariable Long pasajeroId) {
        return ResponseEntity.ok(reservaService.getReservasByPasajeroId(pasajeroId));
    }
}