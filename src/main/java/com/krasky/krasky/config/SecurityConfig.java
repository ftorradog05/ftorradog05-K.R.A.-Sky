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

    // 1. CODIFICADOR DE CONTRASEÑAS (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. PROVEEDOR DE AUTENTICACIÓN
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

    // 4. CONFIGURACIÓN DE FILTROS Y RUTAS
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para simplificar desarrollo
                .authorizeHttpRequests(auth -> auth
                        // === RUTAS PÚBLICAS (ACCESO A TODOS) ===
                        // Importante: "/registro" debe estar aquí para que puedan crearse la cuenta
                        .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/images/**", "/error", "/webjars/**").permitAll()

                        // === RUTAS PROTEGIDAS ===
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")        // Tu página de login personalizada
                        .defaultSuccessUrl("/", true) // Redirigir al inicio al entrar
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirigir al login al salir
                        .permitAll()
                );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}