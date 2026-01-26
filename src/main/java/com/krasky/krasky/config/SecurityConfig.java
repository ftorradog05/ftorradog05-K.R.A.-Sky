package com.krasky.krasky.config;

import com.krasky.krasky.security.UserDetailsServiceImpl;
import com.krasky.krasky.security.jwt.AuthEntryPointJwt;
import com.krasky.krasky.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar @PreAuthorize en los controladores si lo necesitas
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 1. Bean para encriptar contraseñas (BCrypt es el estándar actual)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Bean para gestionar la autenticación (conectar BD con Spring Security)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 3. Bean para obtener el AuthenticationManager (necesario para el login de la API)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ========================================================================
    // CADENA DE SEGURIDAD 1: API REST (/api/**) -> Stateless con JWT
    // ========================================================================
    @Bean
    @Order(1) // Esta cadena se evalúa PRIMERO
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Solo aplica a rutas que empiecen por /api/
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF porque usamos tokens
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Login y Registro API son públicos
                        .requestMatchers("/api/vuelos/disponibles").permitAll() // Ejemplo de endpoint público
                        .requestMatchers("/api/vuelos/buscar").permitAll()
                        .anyRequest().authenticated() // El resto requiere Token
                );

        http.authenticationProvider(authenticationProvider());
        // Añadimos el filtro JWT antes del filtro de usuario/contraseña estándar
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ========================================================================
    // CADENA DE SEGURIDAD 2: WEB MVC (/web/**) -> Con Sesiones y Login Form
    // ========================================================================
    @Bean
    @Order(2) // Esta cadena se evalúa DESPUÉS
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/", "/web/**", "/login", "/logout", "/css/**", "/js/**", "/images/**", "/error")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/error").permitAll() // Recursos públicos
                        .requestMatchers("/web/admin/**").hasRole("ADMIN") // Ejemplo: Solo admin
                        // Aquí definimos que para entrar a /web/... hay que estar autenticado
                        .requestMatchers("/web/**").hasAnyRole("ADMIN", "EMPLEADO", "USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Ruta de nuestro formulario custom
                        .loginProcessingUrl("/login") // Donde se envía el POST del formulario
                        .defaultSuccessUrl("/web/dashboard", true) // A dónde va si el login es correcto
                        .failureUrl("/login?error=true") // A dónde va si falla
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado") // Página personalizada para error 403
                );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}