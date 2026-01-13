package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.VueloDTO;
import com.krasky.krasky.service.AvionService;
import com.krasky.krasky.service.VueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String guardarVuelo(@ModelAttribute("vuelo") VueloDTO vueloDTO) {
        vueloService.saveVuelo(vueloDTO);
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