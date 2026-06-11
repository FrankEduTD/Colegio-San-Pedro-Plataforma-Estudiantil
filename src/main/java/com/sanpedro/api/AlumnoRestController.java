package com.sanpedro.api;

import com.sanpedro.dto.AlumnoDto;
import com.sanpedro.model.Alumno;
import com.sanpedro.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoRestController {

    private final AlumnoService alumnoService;

    public AlumnoRestController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public List<AlumnoDto> listar(
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado) {

        return alumnoService.filtrarAlumnos(termino, estado).stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AlumnoDto obtener(@PathVariable Integer id) {
        return toDto(alumnoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody AlumnoDto dto) {
        if (dto.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("Debe asignar una cuenta de usuario al alumno (usuarioId es obligatorio).");
        }

        try {
            Alumno alumno = new Alumno();
            mapearDatosBasicos(alumno, dto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toDto(alumnoService.guardar(alumno, dto.getUsuarioId())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody AlumnoDto dto) {
        if (dto.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("Debe asignar una cuenta de usuario al alumno (usuarioId es obligatorio).");
        }

        try {
            Alumno alumno = new Alumno();
            alumno.setIdAlumno(id);
            mapearDatosBasicos(alumno, dto);

            return ResponseEntity.ok(toDto(alumnoService.guardar(alumno, dto.getUsuarioId())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        alumnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    // METODOS AUXILIARES DE MAPEO

    private void mapearDatosBasicos(Alumno alumno, AlumnoDto dto) {
        alumno.setDni(dto.getDni());
        alumno.setCodigoEstudiante(dto.getCodigoEstudiante());
        alumno.setNombres(dto.getNombres());
        alumno.setApellidos(dto.getApellidos());
        alumno.setFechaNacimiento(dto.getFechaNacimiento());
        alumno.setTelefonoApoderado(dto.getTelefonoApoderado());
        alumno.setEstado(dto.getEstado() == null || dto.getEstado().isBlank() ? "Activo" : dto.getEstado());
    }

    private AlumnoDto toDto(Alumno alumno) {
        AlumnoDto dto = new AlumnoDto();
        dto.setIdAlumno(alumno.getIdAlumno());
        dto.setUsuarioId(alumno.getUsuario() != null ? alumno.getUsuario().getId() : null);
        dto.setDni(alumno.getDni());
        dto.setCodigoEstudiante(alumno.getCodigoEstudiante());
        dto.setNombres(alumno.getNombres()); // Nombres en plural corregido
        dto.setApellidos(alumno.getApellidos()); // Apellidos en plural corregido
        dto.setFechaNacimiento(alumno.getFechaNacimiento());
        dto.setTelefonoApoderado(alumno.getTelefonoApoderado());
        dto.setEstado(alumno.getEstado());
        return dto;
    }
}