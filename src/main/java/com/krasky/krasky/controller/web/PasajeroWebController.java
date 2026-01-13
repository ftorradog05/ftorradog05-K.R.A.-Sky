package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.PasajeroDTO;
import com.krasky.krasky.service.PasajeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/pasajeros")
public class PasajeroWebController {

    @Autowired
    private PasajeroService pasajeroService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaPasajeros", pasajeroService.getAllPasajeros());
        return "pasajeros/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pasajero", new PasajeroDTO());
        return "pasajeros/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute PasajeroDTO pasajeroDTO) {
        pasajeroService.savePasajero(pasajeroDTO);
        return "redirect:/web/pasajeros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pasajero", pasajeroService.getPasajeroById(id).orElse(new PasajeroDTO()));
        return "pasajeros/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pasajeroService.deletePasajero(id);
        return "redirect:/web/pasajeros";
    }
}