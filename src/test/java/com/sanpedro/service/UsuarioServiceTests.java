package com.sanpedro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sanpedro.model.Usuario;
import com.sanpedro.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void debeCodificarPasswordNueva() {
        Usuario usuario = new Usuario();
        usuario.setUsername("nuevo");
        usuario.setPassword("secreta123");
        usuario.setRol("ADMIN");
        usuario.setEstado("ACTIVO");

        when(passwordEncoder.encode("secreta123")).thenReturn("hash-bcrypt");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario guardado = usuarioService.guardar(usuario);

        assertEquals("hash-bcrypt", guardado.getPassword());
        verify(passwordEncoder).encode("secreta123");
    }

    @Test
    void debeMantenerPasswordActualSiSeActualizaVacia() {
        Usuario existente = new Usuario();
        existente.setId(1);
        existente.setPassword("$2a$12$hashExistenteSimulado123456789012345678901234567890");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("admin");
        usuario.setPassword("");
        usuario.setRol("ADMIN");
        usuario.setEstado("ACTIVO");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario guardado = usuarioService.guardar(usuario);

        assertEquals("$2a$12$hashExistenteSimulado123456789012345678901234567890", guardado.getPassword());
        verify(passwordEncoder, never()).encode(any(String.class));
    }
}