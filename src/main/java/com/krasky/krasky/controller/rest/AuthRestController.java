package com.krasky.krasky.controller.rest;

import com.krasky.krasky.dto.JwtResponse;
import com.krasky.krasky.dto.LoginRequest;
import com.krasky.krasky.dto.RegistroRequest;
import com.krasky.krasky.model.Rol;
import com.krasky.krasky.model.Usuario;
import com.krasky.krasky.repository.RolRepository;
import com.krasky.krasky.repository.UsuarioRepository;
import com.krasky.krasky.security.UserDetailsImpl;
import com.krasky.krasky.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. Autenticamos al usuario con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Guardamos la autenticación en el contexto
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generamos el Token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Obtenemos los datos del usuario para devolverlos
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 5. Devolvemos la respuesta con el Token
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistroRequest signUpRequest) {
        // Validar si existe username o email
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: ¡El username ya está en uso!");
        }

        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: ¡El email ya está en uso!");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())); // ¡Importante encriptar!

        usuario.setNombre(signUpRequest.getNombre());
        usuario.setApellidos(signUpRequest.getApellidos());

        // Asignar roles (Por defecto USER si no se especifica)
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null) {
            Rol userRole = rolRepository.findByNombre(Rol.RolNombre.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Rol adminRole = rolRepository.findByNombre(Rol.RolNombre.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRole);
                        break;
                    case "empleado":
                        Rol modRole = rolRepository.findByNombre(Rol.RolNombre.ROLE_EMPLEADO)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(modRole);
                        break;
                    default:
                        Rol userRole = rolRepository.findByNombre(Rol.RolNombre.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(userRole);
                }
            });
        }

        usuario.setRoles(roles);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}