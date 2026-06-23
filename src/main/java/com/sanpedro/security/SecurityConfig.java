package com.sanpedro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults()) // Habilita tu configuración de CORS para Angular
                .csrf(csrf -> csrf.disable())    // En APIs con JWT, CSRF debe estar deshabilitado

                // REGLAS DE AUTORIZACIÓN

                .authorizeHttpRequests(auth -> auth
                        // Públicos
                        .requestMatchers("/api/auth/login", "/css/**", "/js/**").permitAll()

                        // Módulo Usuarios
                        .requestMatchers("/api/usuarios/**").hasRole("admin")

                        // Endpoints GET para usuarios autenticados (Admin, Docente, Alumno)
                        .requestMatchers(HttpMethod.GET,
                                "/api/alumnos/**", "/api/docentes/**", "/api/cursos/**", "/api/matriculas/**"
                        ).authenticated()

                        // Endpoints de Escritura (POST, PUT, DELETE) solo para ADMIN
                        .requestMatchers(HttpMethod.POST,
                                "/api/alumnos/**", "/api/docentes/**", "/api/cursos/**", "/api/matriculas/**"
                        ).hasRole("admin")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/alumnos/**", "/api/docentes/**", "/api/cursos/**", "/api/matriculas/**"
                        ).hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/alumnos/**", "/api/docentes/**", "/api/cursos/**", "/api/matriculas/**"
                        ).hasRole("admin")

                        // Módulo de Notas / Detalle
                        .requestMatchers("/api/detalle-matriculas/**").hasAnyRole("admin", "docente")

                        // Cualquier otra ruta API pide estar autenticado
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )


                // CONFIGURACIÓN API REST
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )


                // PROVEEDORES Y FILTROS

                .authenticationProvider(authenticationProvider())

                // Nuestro filtro JWT intercepta las peticiones ANTES de revisar las contraseñas
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}