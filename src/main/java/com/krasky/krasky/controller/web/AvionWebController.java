package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.AvionDTO;
import com.krasky.krasky.service.AvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String guardarAvion(@ModelAttribute AvionDTO avionDTO) {
        avionService.saveAvion(avionDTO);
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