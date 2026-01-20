package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.VueloDTO;
import com.krasky.krasky.exception.BusinessException;
import com.krasky.krasky.service.AvionService;
import com.krasky.krasky.service.VueloService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/vuelos")
public class VueloWebController {

    @Autowired
    private VueloService vueloService;

    @Autowired
    private AvionService avionService;

    @GetMapping
    public String listarVuelos(Model model) {
        model.addAttribute("listaVuelos", vueloService.getAllVuelos());
        return "vuelos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vuelo", new VueloDTO());
        model.addAttribute("listaAviones", avionService.getAllAviones());
        return "vuelos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarVuelo(@Valid @ModelAttribute("vuelo") VueloDTO vueloDTO, BindingResult result, Model model) {
        // 1. Si hay errores de validación (ej: precio negativo), volvemos al formulario
        if (result.hasErrors()) {
            // Recargamos las listas necesarias para los select (aviones, etc.)
            model.addAttribute("listaAviones", avionService.getAllAviones());
            return "vuelos/formulario"; // NO redirigimos, volvemos a la vista para mostrar errores
        }

        // 2. Si todo está bien, guardamos
        try {
            vueloService.saveVuelo(vueloDTO);
        } catch (BusinessException e) {
            // Capturamos errores de negocio (ej: vuelo duplicado) y los añadimos al formulario
            result.rejectValue("numeroVuelo", "error.vuelo", e.getMessage());
            model.addAttribute("listaAviones", avionService.getAllAviones());
            return "vuelos/formulario";
        }

        return "redirect:/web/vuelos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        VueloDTO dto = vueloService.getVueloById(id).orElse(new VueloDTO());
        model.addAttribute("vuelo", dto);
        model.addAttribute("listaAviones", avionService.getAllAviones());
        return "vuelos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVuelo(@PathVariable Long id) {
        vueloService.deleteVuelo(id);
        return "redirect:/web/vuelos";
    }
}