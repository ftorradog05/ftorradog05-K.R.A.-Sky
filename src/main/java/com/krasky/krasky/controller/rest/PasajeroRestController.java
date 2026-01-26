package com.krasky.krasky.controller.rest;

import com.krasky.krasky.dto.PasajeroDTO;
import com.krasky.krasky.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroRestController {

    @Autowired
    private PasajeroService pasajeroService;

    @GetMapping
    public ResponseEntity<List<PasajeroDTO>> getAllPasajeros() {
        return ResponseEntity.ok(pasajeroService.getAllPasajeros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasajeroDTO> getPasajeroById(@PathVariable Long id) {
        return pasajeroService.getPasajeroById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PasajeroDTO> createPasajero(@Valid @RequestBody PasajeroDTO pasajeroDTO) {
        return ResponseEntity.ok(pasajeroService.savePasajero(pasajeroDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePasajero(@PathVariable Long id) {
        pasajeroService.deletePasajero(id);
        return ResponseEntity.noContent().build();
    }

    // En PasajeroRestController.java:

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PasajeroDTO> getPasajeroByDni(@PathVariable String dni) {
        return pasajeroService.getPasajeroByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasajeroDTO> updatePasajero(@PathVariable Long id, @Valid @RequestBody PasajeroDTO pasajeroDTO) {
        return ResponseEntity.ok(pasajeroService.updatePasajero(id, pasajeroDTO));
    }
}