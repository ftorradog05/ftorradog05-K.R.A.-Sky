package com.krasky.krasky.controller;

import com.krasky.krasky.model.Rol;
import com.krasky.krasky.model.Usuario;
import com.krasky.krasky.repository.RolRepository;
import com.krasky.krasky.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. MOSTRAR EL FORMULARIO DE REGISTRO
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // Nombre del archivo HTML (registro.html)
    }

    // 2. GUARDAR EL NUEVO USUARIO (CLIENTE)
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {

        // A. Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // B. Asignar ROL DE USUARIO (Automático)
        // Buscamos el rol 'ROLE_USER' en la base de datos
        Rol rolUser = rolRepository.findByNombre(Rol.RolNombre.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Rol de usuario no encontrado."));

        // Se lo asignamos al usuario
        usuario.setRoles(Collections.singleton(rolUser));

        // C. Activar usuario y otros datos por defecto
        usuario.setEnabled(true);
        if (usuario.getApellidos() == null) usuario.setApellidos(""); // Para que no sea null

        // D. Guardar
        usuarioRepository.save(usuario);

        // Redirigir al login con un mensaje de éxito
        return "redirect:/login?registrado";
    }
}