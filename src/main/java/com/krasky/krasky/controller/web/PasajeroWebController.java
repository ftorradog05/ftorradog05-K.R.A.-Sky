package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.PasajeroDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.service.PasajeroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String guardar(@Valid @ModelAttribute("pasajero") PasajeroDTO pasajeroDTO, BindingResult result, Model model) {
        // 1. Validaciones de formato (@NotBlank, @Email, Pattern DNI)
        if (result.hasErrors()) {
            return "pasajeros/formulario";
        }

        try {
            // 2. Intentar guardar
            pasajeroService.savePasajero(pasajeroDTO);
        } catch (BusinessException e) {
            // 3. Capturar errores de negocio (DNI o Email duplicado)
            // Asignamos el error al campo 'dni' o global si prefieres
            // Aquí lo pongo global para asegurar que se vea, o podrías intentar detectar si es el email o dni
            result.rejectValue("dni", "error.pasajero", e.getMessage());
            return "pasajeros/formulario";
        }

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