package com.sanpedro.api;

import java.util.List;

import com.sanpedro.dto.UsuarioDto;
import com.sanpedro.model.Usuario;
import com.sanpedro.service.UsuarioService;

import jakarta.validation.Valid; // Importante para las validaciones del DTO
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDto> listar(
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado) {

        return usuarioService.filtrarUsuarios(termino, estado).stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioDto obtener(@PathVariable Integer id) {
        return toDto(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody UsuarioDto dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank() || dto.getPassword().length() < 8) {
            return ResponseEntity.badRequest().body("La contraseña es obligatoria al crear y debe tener al menos 8 caracteres.");
        }
        try {
            Usuario usuario = new Usuario();
            usuario.setUsername(dto.getUsername());
            usuario.setEmail(dto.getEmail()); // Faltaba mapear el email
            usuario.setPassword(dto.getPassword());
            usuario.setRol(dto.getRol());
            usuario.setEstado(dto.getEstado() == null || dto.getEstado().isBlank() ? "Activo" : dto.getEstado());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toDto(usuarioService.guardar(usuario)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id,
                                        @Valid @RequestBody UsuarioDto dto) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            usuario.setUsername(dto.getUsername());
            usuario.setEmail(dto.getEmail());

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPassword(dto.getPassword());
            }

            usuario.setRol(dto.getRol());

            if (dto.getEstado() != null && !dto.getEstado().isBlank()) {
                usuario.setEstado(dto.getEstado());
            }

            return ResponseEntity.ok(toDto(usuarioService.guardar(usuario)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioDto toDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail()); // Faltaba devolver el email
        dto.setRol(usuario.getRol());
        dto.setEstado(usuario.getEstado());
        return dto;
    }
}