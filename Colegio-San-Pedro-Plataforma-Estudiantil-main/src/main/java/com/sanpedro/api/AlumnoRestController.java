package com.sanpedro.api;

import com.sanpedro.dto.*;
import com.sanpedro.model.Alumno;
import com.sanpedro.model.CursoProgramado;
import com.sanpedro.model.Matricula;
import com.sanpedro.repository.CursoProgramadoRepository;
import com.sanpedro.repository.MatriculaRepository;
import com.sanpedro.service.AlumnoCalificacionService;
import com.sanpedro.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoRestController {

    private final AlumnoService alumnoService;
    private final AlumnoCalificacionService alumnoCalificacionService;
    private final MatriculaRepository matriculaRepository;
    private final CursoProgramadoRepository cursoProgramadoRepository;

    public AlumnoRestController(AlumnoService alumnoService, 
                               AlumnoCalificacionService alumnoCalificacionService,
                               MatriculaRepository matriculaRepository,
                               CursoProgramadoRepository cursoProgramadoRepository) {
        this.alumnoService = alumnoService;
        this.alumnoCalificacionService = alumnoCalificacionService;
        this.matriculaRepository = matriculaRepository;
        this.cursoProgramadoRepository = cursoProgramadoRepository;
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

    @GetMapping("/usuario/{username}")
    public AlumnoDto obtenerPorUsername(@PathVariable String username) {
        return toDto(alumnoService.obtenerPorUsername(username));
    }

    @GetMapping("/usuario/{username}/perfil")
    public AlumnoPerfilDto obtenerPerfilPorUsername(@PathVariable String username,
                                                    @RequestParam(required = false) String periodo) {
        Alumno alumno = alumnoService.obtenerPorUsername(username);
        return alumnoCalificacionService.obtenerPerfil(alumno.getIdAlumno(), periodo);
    }

    @GetMapping("/usuario/{username}/cursos")
    public List<AlumnoCursoNotasDto> listarCursosPorUsername(@PathVariable String username,
                                                             @RequestParam(required = false) String periodo) {
        Alumno alumno = alumnoService.obtenerPorUsername(username);
        return alumnoCalificacionService.listarCursosDelAlumno(alumno.getIdAlumno(), periodo);
    }

    @GetMapping("/usuario/{username}/notas")
    public List<AlumnoCursoNotasDto> listarNotasPorUsername(@PathVariable String username,
                                                            @RequestParam(required = false) String periodo) {
        Alumno alumno = alumnoService.obtenerPorUsername(username);
        return alumnoCalificacionService.listarNotasDelAlumno(alumno.getIdAlumno(), periodo);
    }

    @GetMapping("/me")
    public AlumnoDto obtenerMiPerfil(Principal principal) {
        Alumno alumno = alumnoService.obtenerPorUsername(principal.getName());
        return toDto(alumno);
    }

    @GetMapping("/me/perfil")
    public AlumnoPerfilDto obtenerPerfilMiAlumno(Principal principal,
                                                 @RequestParam(required = false) String periodo) {
        Alumno alumno = alumnoService.obtenerPorUsername(principal.getName());
        return alumnoCalificacionService.obtenerPerfil(alumno.getIdAlumno(), periodo);
    }

    @GetMapping("/me/cursos")
    public List<AlumnoCursoNotasDto> listarCursosMiAlumno(Principal principal,
                                                          @RequestParam(required = false) String periodo) {
        Alumno alumno = alumnoService.obtenerPorUsername(principal.getName());
        return alumnoCalificacionService.listarCursosDelAlumno(alumno.getIdAlumno(), periodo);
    }

    @GetMapping("/me/notas")
    public List<AlumnoCursoNotasDto> listarNotasMiAlumno(Principal principal,
                                                         @RequestParam(required = false) String periodo) {
        Alumno alumno = alumnoService.obtenerPorUsername(principal.getName());
        return alumnoCalificacionService.listarNotasDelAlumno(alumno.getIdAlumno(), periodo);
    }

    // Mi Aula - Compañeros y Docentes
    @GetMapping("/mi-aula/compañeros")
    public List<AlumnoCompañeroDto> listarCompañeros(Principal principal) {
        try {
            Alumno alumno = alumnoService.obtenerPorUsername(principal.getName());
            List<Matricula> miMatricula = matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());
            
            if (miMatricula.isEmpty()) {
                return List.of();
            }
            
            Integer aulaId = miMatricula.get(0).getAula().getIdAula();
            List<Matricula> matriculas = matriculaRepository.findByAula_IdAula(aulaId);
            
            return matriculas.stream()
                    .map(m -> new AlumnoCompañeroDto(
                        m.getAlumno().getIdAlumno(),
                        m.getAlumno().getNombres(),
                        m.getAlumno().getApellidos(),
                        m.getAlumno().getCodigoEstudiante(),
                        m.getAlumno().getDni()
                    ))
                    .toList();
        } catch (Exception e) {
            System.err.println("Error en /mi-aula/compañeros: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al cargar compañeros: " + e.getMessage());
        }
    }

    @GetMapping("/mi-aula/docentes")
    public List<DocenteAulaDto> listarDocentes(Principal principal) {
        try {
            Alumno alumno = alumnoService.obtenerPorUsername(principal.getName());
            List<Matricula> miMatricula = matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());
            
            if (miMatricula.isEmpty()) {
                return List.of();
            }
            
            Integer aulaId = miMatricula.get(0).getAula().getIdAula();
            List<CursoProgramado> cursos = cursoProgramadoRepository.findByAulaIdAula(aulaId);
            
            // Usar Set para evitar duplicados (mismo docente enseña múltiples materias)
            Set<DocenteAulaDto> docentesUnicos = cursos.stream()
                    .map(c -> new DocenteAulaDto(
                        c.getDocente().getIdDocente(),
                        c.getDocente().getNombres(),
                        c.getDocente().getApellidos(),
                        c.getMateria().getNombre(),
                        c.getDocente().getUsuario().getEmail(),
                        c.getDocente().getTelefono()
                    ))
                    .collect(Collectors.toSet());
            
            return docentesUnicos.stream().toList();
        } catch (Exception e) {
            System.err.println("Error en /mi-aula/docentes: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al cargar docentes: " + e.getMessage());
        }
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