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
        // Buscamos al usuario en la BBDD
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // --- CORRECCIÓN CRÍTICA ---
        // Usamos TU clase UserDetailsImpl para construir el objeto.
        // Esto devuelve un UserDetailsImpl que el controlador sí puede entender.
        return com.krasky.krasky.security.UserDetailsImpl.build(usuario);
    }
}