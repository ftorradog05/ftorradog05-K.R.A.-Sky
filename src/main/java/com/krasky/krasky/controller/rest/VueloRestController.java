package com.krasky.krasky.controller.rest;

import com.krasky.krasky.dto.VueloDTO;
import com.krasky.krasky.service.VueloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
public class VueloRestController {

    @Autowired
    private VueloService vueloService;

    @GetMapping
    public ResponseEntity<List<VueloDTO>> getAllVuelos() {
        return ResponseEntity.ok(vueloService.getAllVuelos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VueloDTO> getVueloById(@PathVariable Long id) {
        return vueloService.getVueloById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VueloDTO> createVuelo(@Valid @RequestBody VueloDTO vueloDTO) {
        return ResponseEntity.ok(vueloService.saveVuelo(vueloDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVuelo(@PathVariable Long id) {
        vueloService.deleteVuelo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VueloDTO> updateVuelo(@PathVariable Long id, @Valid @RequestBody VueloDTO vueloDTO) {
        return ResponseEntity.ok(vueloService.updateVuelo(id, vueloDTO));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<VueloDTO>> getVuelosDisponibles() {
        return ResponseEntity.ok(vueloService.getVuelosDisponibles());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<VueloDTO>> buscarVuelos(
            @RequestParam String origen,
            @RequestParam String destino) {
        return ResponseEntity.ok(vueloService.buscarPorOrigenYDestino(origen, destino));
    }
}