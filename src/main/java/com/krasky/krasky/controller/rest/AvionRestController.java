package com.krasky.krasky.controller.rest;

import com.krasky.krasky.dto.AvionDTO;
import com.krasky.krasky.service.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aviones") // <--- ESTA ES LA CLAVE PARA QUE FUNCIONE EL LINK
public class AvionRestController {

    @Autowired
    private AvionService avionService;

    // 1. GET: Ver todos los aviones
    @GetMapping
    public ResponseEntity<List<AvionDTO>> getAllAviones() {
        return ResponseEntity.ok(avionService.getAllAviones());
    }

    // 2. GET: Ver un avión por ID
    @GetMapping("/{id}")
    public ResponseEntity<AvionDTO> getAvionById(@PathVariable Long id) {
        return avionService.getAvionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST: Crear un avión
    @PostMapping
    public ResponseEntity<AvionDTO> createAvion(@RequestBody AvionDTO avionDTO) {
        return ResponseEntity.ok(avionService.saveAvion(avionDTO));
    }

    // 4. DELETE: Borrar un avión
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvion(@PathVariable Long id) {
        avionService.deleteAvion(id);
        return ResponseEntity.noContent().build();
    }
}