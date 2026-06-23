package com.sanpedro.service;

import com.sanpedro.model.Docente;
import com.sanpedro.model.Usuario;
import com.sanpedro.repository.DocenteRepository;
import com.sanpedro.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocenteService {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;

    public DocenteService(DocenteRepository docenteRepository, UsuarioRepository usuarioRepository) {
        this.docenteRepository = docenteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Docente> filtrarDocentes(String termino, String estado) {
        return docenteRepository.filtrarDocentes(termino, estado);
    }

    public Docente obtenerPorId(Integer id) {
        return docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado con ID: " + id));
    }

    public Docente obtenerPorUsername(String username) {
        return docenteRepository.findByUsuario_Username(username)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado para el usuario: " + username));
    }

    // Recibimos la entidad Docente y el ID del Usuario que viene del desplegable
    public Docente guardar(Docente docente, Integer usuarioId) {

        // 1. Buscamos el usuario en la BD para vincularlo
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("El usuario seleccionado no existe."));

        // 2. Seguridad extra: Validamos que el usuario elegido realmente sea de perfil 'docente'
        if (!usuario.getRol().equals("docente")) {
            throw new RuntimeException("Error: El usuario seleccionado no tiene el perfil de docente.");
        }

        if (docente.getIdDocente() == null) {
            // ================= LÓGICA DE CREACIÓN =================
            if (docenteRepository.existsByDni(docente.getDni())) {
                throw new RuntimeException("Error: El DNI " + docente.getDni() + " ya está registrado en el sistema.");
            }
            if (docenteRepository.existsByUsuarioIdUsuario(usuarioId)) {
                throw new RuntimeException("Error: Este usuario ya está vinculado a otro profesor.");
            }
            docente.setEstado("Activo");
        } else {
            // ================= LÓGICA DE ACTUALIZACIÓN =================
            Docente existente = obtenerPorId(docente.getIdDocente());

            if (!existente.getDni().equals(docente.getDni()) && docenteRepository.existsByDni(docente.getDni())) {
                throw new RuntimeException("Error: El DNI " + docente.getDni() + " le pertenece a otro profesor.");
            }

            // Si el admin decide cambiar el usuario asignado, verificamos que el nuevo no esté ocupado
            if (!existente.getUsuario().getId().equals(usuarioId) && docenteRepository.existsByUsuarioIdUsuario(usuarioId)) {
                throw new RuntimeException("Error: El nuevo usuario seleccionado ya está vinculado a otro profesor.");
            }
        }

        // 3. Establecemos la relación 1:1 y guardamos
        docente.setUsuario(usuario);
        return docenteRepository.save(docente);
    }

    public void eliminar(Integer id) {
        Docente docente = obtenerPorId(id);
        docente.setEstado("Inactivo");
        docenteRepository.save(docente);
    }
}