package com.krasky.krasky.controller.web;

import com.krasky.krasky.model.Vuelo;
import com.krasky.krasky.repository.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private VueloRepository vueloRepository;

    @GetMapping("/")
    public String redireccionarPorRol(
            Authentication auth,
            Model model,
            // Quitamos el parámetro fecha, volvemos a lo seguro
            @RequestParam(name = "origen", required = false) String origen,
            @RequestParam(name = "destino", required = false) String destino
    ) {
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "index";
        } else {
            List<Vuelo> vuelos;

            // Lógica simple: Si hay texto, buscamos. Si no, todo.
            if ((origen != null && !origen.isEmpty()) || (destino != null && !destino.isEmpty())) {
                vuelos = vueloRepository.buscarVuelos(origen, destino);
            } else {
                vuelos = vueloRepository.findAll();
            }

            model.addAttribute("listaVuelos", vuelos);
            return "cliente_home";
        }
    }
}