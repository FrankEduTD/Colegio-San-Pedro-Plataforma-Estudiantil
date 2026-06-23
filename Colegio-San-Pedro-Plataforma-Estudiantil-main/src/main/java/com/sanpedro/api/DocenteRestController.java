package com.sanpedro.api;

import com.sanpedro.dto.DocenteDto;
import com.sanpedro.dto.DocenteDashboardDto;
import com.sanpedro.dto.DocenteCursoAulaDto;
import com.sanpedro.dto.CursoRosterAlumnoDto;
import com.sanpedro.dto.AlumnoCalificacionDto;
import com.sanpedro.dto.CalificacionResponseDto;
import com.sanpedro.dto.EdicionNotaDto;
import com.sanpedro.dto.HorarioDto;
import com.sanpedro.dto.DocenteTutoriaDto;
import com.sanpedro.model.Docente;
import com.sanpedro.service.DocenteDashboardService;
import com.sanpedro.service.DocenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
public class DocenteRestController {

    private final DocenteService docenteService;
    private final DocenteDashboardService docenteDashboardService;

    public DocenteRestController(DocenteService docenteService, DocenteDashboardService docenteDashboardService) {
        this.docenteService = docenteService;
        this.docenteDashboardService = docenteDashboardService;
    }

    @GetMapping("/me/dashboard")
    public DocenteDashboardDto obtenerMiDashboard(Principal principal) {
        return docenteDashboardService.obtenerDashboard(principal.getName());
    }

    @GetMapping("/me/cursos-aulas")
    public List<DocenteCursoAulaDto> listarMisCursosAulas(Principal principal) {
        return docenteDashboardService.listarMisCursosAulas(principal.getName());
    }

    @GetMapping("/me/horario-semanal")
    public List<HorarioDto> obtenerMiHorarioSemanal(Principal principal) {
        return docenteDashboardService.listarMiHorarioSemanal(principal.getName());
    }

    @GetMapping("/me/tutoria")
    public DocenteTutoriaDto obtenerMiTutoria(Principal principal) {
        return docenteDashboardService.obtenerMiTutoria(principal.getName());
    }

    @GetMapping("/me/cursos-aulas/{cursoProgId}/alumnos")
    public List<CursoRosterAlumnoDto> listarAlumnosDeMiCurso(Principal principal,
                                                              @PathVariable Integer cursoProgId) {
        return docenteDashboardService.listarRosterDeCurso(principal.getName(), cursoProgId);
    }

    @GetMapping("/me/cursos-aulas/{cursoProgId}/bimestre/{numeroBimestre}/planilla")
    public List<AlumnoCalificacionDto> obtenerPlanillaDeMiCurso(Principal principal,
                                                                 @PathVariable Integer cursoProgId,
                                                                 @PathVariable Integer numeroBimestre) {
        return docenteDashboardService.obtenerPlanillaCalificaciones(principal.getName(), cursoProgId, numeroBimestre);
    }

    @PutMapping("/me/calificaciones/detalle/{idDetalle}/bimestre/{numeroBimestre}")
    public CalificacionResponseDto guardarNotaEnMiCurso(Principal principal,
                                                        @PathVariable Integer idDetalle,
                                                        @PathVariable Integer numeroBimestre,
                                                        @RequestBody EdicionNotaDto dto) {
        return docenteDashboardService.guardarNotaDocente(principal.getName(), idDetalle, numeroBimestre, dto);
    }

    @GetMapping
    public List<DocenteDto> listar(
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado) {

        return docenteService.filtrarDocentes(termino, estado).stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public DocenteDto obtener(@PathVariable Integer id) {
        return toDto(docenteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody DocenteDto dto) {
        if (dto.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("Debe asignar una cuenta de usuario al docente (usuarioId es obligatorio).");
        }

        try {
            Docente docente = new Docente();
            mapearDatosBasicos(docente, dto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toDto(docenteService.guardar(docente, dto.getUsuarioId())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody DocenteDto dto) {
        if (dto.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("Debe asignar una cuenta de usuario al docente (usuarioId es obligatorio).");
        }

        try {
            Docente docente = new Docente();
            docente.setIdDocente(id);
            mapearDatosBasicos(docente, dto);

            return ResponseEntity.ok(toDto(docenteService.guardar(docente, dto.getUsuarioId())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        docenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void mapearDatosBasicos(Docente docente, DocenteDto dto) {
        docente.setDni(dto.getDni());
        docente.setNombres(dto.getNombres());
        docente.setApellidos(dto.getApellidos());
        docente.setEspecialidad(dto.getEspecialidad());
        docente.setTelefono(dto.getTelefono());
        docente.setEstado(dto.getEstado() == null || dto.getEstado().isBlank() ? "Activo" : dto.getEstado());
    }

    private DocenteDto toDto(Docente docente) {
        DocenteDto dto = new DocenteDto();
        dto.setIdDocente(docente.getIdDocente());
        dto.setUsuarioId(docente.getUsuario().getId());
        dto.setDni(docente.getDni());
        dto.setNombres(docente.getNombres());
        dto.setApellidos(docente.getApellidos());
        dto.setEspecialidad(docente.getEspecialidad());
        dto.setTelefono(docente.getTelefono());
        dto.setEstado(docente.getEstado());
        return dto;
    }
}