package com.krasky.krasky.security;

import com.krasky.krasky.model.Usuario;
import com.krasky.krasky.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // CHIVATO 1: ¿Llega la petición?
        System.out.println(">>> DEBUG: Intentando login con usuario: " + username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println(">>> DEBUG: ¡Usuario NO encontrado en la BD!");
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        // CHIVATO 2: ¿Qué contraseña tiene en la BD?
        System.out.println(">>> DEBUG: Usuario encontrado. ID: " + usuario.getId());
        System.out.println(">>> DEBUG: Contraseña (Hash) en BD: " + usuario.getPassword());

        // Construimos el usuario correctamente
        return UserDetailsImpl.build(usuario);
    }
}