package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.AvionDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.service.AvionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/aviones")
public class AvionWebController {

    @Autowired
    private AvionService avionService;

    @GetMapping
    public String listarAviones(Model model) {
        model.addAttribute("listaAviones", avionService.getAllAviones());
        return "aviones/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("avion", new AvionDTO());
        return "aviones/formulario";
    }

    @PostMapping("/guardar")
    public String guardarAvion(@Valid @ModelAttribute("avion") AvionDTO avionDTO, BindingResult result, Model model) {
        // 1. Validaciones de formato (@NotBlank, @Min capacidad)
        if (result.hasErrors()) {
            return "aviones/formulario";
        }

        try {
            // 2. Intentar guardar
            avionService.saveAvion(avionDTO);
        } catch (BusinessException e) {
            // 3. Capturar error de negocio (Matrícula duplicada)
            result.rejectValue("matricula", "error.avion", e.getMessage());
            return "aviones/formulario";
        }

        return "redirect:/web/aviones";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        model.addAttribute("avion", avionService.getAvionById(id).orElse(new AvionDTO()));
        return "aviones/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAvion(@PathVariable Long id) {
        avionService.deleteAvion(id);
        return "redirect:/web/aviones";
    }
}