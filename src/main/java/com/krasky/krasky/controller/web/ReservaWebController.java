package com.krasky.krasky.controller.web;

import com.krasky.krasky.dto.ReservaDTO;
import com.krasky.krasky.service.PasajeroService;
import com.krasky.krasky.service.ReservaService;
import com.krasky.krasky.service.VueloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/reservas")
public class ReservaWebController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private VueloService vueloService;
    @Autowired
    private PasajeroService pasajeroService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaReservas", reservaService.getAllReservas());
        return "reservas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("reserva", new ReservaDTO());
        model.addAttribute("listaVuelos", vueloService.getAllVuelos());
        model.addAttribute("listaPasajeros", pasajeroService.getAllPasajeros());
        return "reservas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ReservaDTO reservaDTO) {
        reservaService.saveReserva(reservaDTO);
        return "redirect:/web/reservas";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("reserva", reservaService.getReservaById(id).orElse(new ReservaDTO()));
        model.addAttribute("listaVuelos", vueloService.getAllVuelos());
        model.addAttribute("listaPasajeros", pasajeroService.getAllPasajeros());
        return "reservas/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        reservaService.deleteReserva(id);
        return "redirect:/web/reservas";
    }
}