package com.krasky.krasky.config;

import com.krasky.krasky.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 1. BEAN PARA ENCRIPTAR CONTRASEÑAS (IMPRESCINDIBLE)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. CONECTAMOS TU SERVICIO DE USUARIOS CON LA SEGURIDAD
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // 3. GESTOR DE AUTENTICACIÓN
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 4. CONFIGURACIÓN DE RUTAS (QUÉ ES PÚBLICO Y QUÉ NO)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivado para facilitar pruebas (activar en producción)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll() // Login y recursos son públicos
                        .anyRequest().authenticated() // Todo lo demás requiere contraseña
                )
                .formLogin(form -> form
                        .loginPage("/login") // La ruta de tu controlador para el login
                        .defaultSuccessUrl("/", true) // Si entra bien, va al inicio
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Al salir vuelve al login
                        .permitAll()
                );

        // Activamos nuestro proveedor
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}