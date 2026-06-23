package com.sanpedro.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sanpedro.model.Usuario;
import com.sanpedro.repository.UsuarioRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 1. NUEVO: Llamada a nuestro filtro dinámico del Repository
    public List<Usuario> filtrarUsuarios(String termino, String estado) {
        return usuarioRepository.filtrarUsuarios(termino, estado);
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario guardar(Usuario usuario) {
        // Validaciones previas para usuarios NUEVOS
        if (usuario.getId() == null) {
            if (usuarioRepository.existsByUsername(usuario.getUsername())) {
                throw new RuntimeException("Error: El nombre de usuario ya está en uso.");
            }
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("Error: El correo electrónico ya está en uso.");
            }
            usuario.setEstado("Activo"); // Aseguramos el estado inicial
        } else {
            // Lógica para usuarios EXISTENTES (Actualización)
            Usuario existente = obtenerPorId(usuario.getId());

            // Validar que el nuevo username o email no choque con OTRO usuario
            if (!existente.getUsername().equals(usuario.getUsername()) && usuarioRepository.existsByUsername(usuario.getUsername())) {
                throw new RuntimeException("Error: El nombre de usuario ya está siendo usado por otra persona.");
            }
            if (!existente.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new RuntimeException("Error: El correo electrónico ya está siendo usado por otra persona.");
            }

            // Si envían la contraseña vacía al actualizar, mantenemos la que ya tenía
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                usuario.setPassword(existente.getPassword());
            }

            // Mantenemos la fecha de creación original intacta
            usuario.setFechaCreacion(existente.getFechaCreacion());
        }

        // Si hay una contraseña (nueva o sin encriptar), le aplicamos BCrypt
        if (usuario.getPassword() != null
                && !usuario.getPassword().isBlank()
                && !esPasswordBCrypt(usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    // 2. MODIFICADO: Borrado lógico cambiando el estado a "Inactivo"
    public void eliminar(Integer id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado("Inactivo");
        usuarioRepository.save(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    public List<Usuario> listarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Page<Usuario> listarPaginado(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    private boolean esPasswordBCrypt(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}