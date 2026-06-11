package com.sanpedro.api;

import com.sanpedro.dto.AuthResponseDto;
import com.sanpedro.dto.LoginRequestDto;
import com.sanpedro.model.Usuario;
import com.sanpedro.repository.UsuarioRepository;
import com.sanpedro.security.JwtService;
import com.sanpedro.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// AQUÍ ESTÁN LAS IMPORTACIONES CORREGIDAS
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {

        // validar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Cargar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());

        // Generar token
        String token = jwtService.generarToken(userDetails);

        // Respuesta
        AuthResponseDto response = new AuthResponseDto(token, usuario.getUsername(), usuario.getRol());
        return ResponseEntity.ok(response);
    }
}