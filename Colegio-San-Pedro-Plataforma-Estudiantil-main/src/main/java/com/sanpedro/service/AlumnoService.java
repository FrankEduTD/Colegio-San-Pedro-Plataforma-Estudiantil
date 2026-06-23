package com.sanpedro.service;

import com.sanpedro.model.Alumno;
import com.sanpedro.model.Usuario;
import com.sanpedro.repository.AlumnoRepository;
import com.sanpedro.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, UsuarioRepository usuarioRepository) {
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Alumno> filtrarAlumnos(String termino, String estado) {
        return alumnoRepository.filtrarAlumnos(termino, estado);
    }

    public Alumno obtenerPorId(Integer id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
    }

    public Alumno obtenerPorUsername(String username) {
        return alumnoRepository.findByUsuario_Username(username)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con username: " + username));
    }

    public Alumno guardar(Alumno alumno, Integer usuarioId) {

        // 1. Buscamos el usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("El usuario seleccionado no existe."));

        // 2. Validamos el rol
        if (!usuario.getRol().equalsIgnoreCase("alumno")) {
            throw new RuntimeException("Error: El usuario seleccionado no tiene el perfil de alumno.");
        }

        if (alumno.getIdAlumno() == null) {
            // === LÓGICA DE CREACIÓN ===
            if (alumnoRepository.existsByDni(alumno.getDni())) {
                throw new RuntimeException("Error: El DNI ya está registrado en el sistema.");
            }
            if (alumno.getCodigoEstudiante() != null && alumnoRepository.existsByCodigoEstudiante(alumno.getCodigoEstudiante())) {
                throw new RuntimeException("Error: El código de estudiante ya existe.");
            }
            if (alumnoRepository.existsByUsuarioIdUsuario(usuarioId)) {
                throw new RuntimeException("Error: Este usuario ya está vinculado a otro alumno.");
            }
            alumno.setEstado("Activo");
        } else {
            // === LÓGICA DE ACTUALIZACIÓN ===
            Alumno existente = obtenerPorId(alumno.getIdAlumno());

            if (!existente.getDni().equals(alumno.getDni()) && alumnoRepository.existsByDni(alumno.getDni())) {
                throw new RuntimeException("Error: El DNI le pertenece a otro alumno.");
            }
            if (alumno.getCodigoEstudiante() != null &&
                    !alumno.getCodigoEstudiante().equals(existente.getCodigoEstudiante()) &&
                    alumnoRepository.existsByCodigoEstudiante(alumno.getCodigoEstudiante())) {
                throw new RuntimeException("Error: El código de estudiante ya le pertenece a otro alumno.");
            }
            if (!existente.getUsuario().getId().equals(usuarioId) && alumnoRepository.existsByUsuarioIdUsuario(usuarioId)) {
                throw new RuntimeException("Error: El nuevo usuario seleccionado ya está vinculado a otro alumno.");
            }
        }

        // 3. Establecemos la relación y guardamos
        alumno.setUsuario(usuario);
        return alumnoRepository.save(alumno);
    }

    public void eliminar(Integer id) {
        Alumno alumno = obtenerPorId(id);
        alumno.setEstado("Inactivo");
        alumnoRepository.save(alumno);
    }
}