package com.sanpedro.service;

import com.sanpedro.dto.AlumnoCursoNotasDto;
import com.sanpedro.dto.AlumnoPerfilDto;
import com.sanpedro.dto.CalificacionResponseDto;
import com.sanpedro.model.CalificacionBimestral;
import com.sanpedro.model.DetalleMatricula;
import com.sanpedro.model.Materia;
import com.sanpedro.model.Docente;
import com.sanpedro.model.Aula;
import com.sanpedro.repository.CalificacionBimestralRepository;
import com.sanpedro.repository.DetalleMatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlumnoCalificacionService {

    private final AlumnoService alumnoService;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final CalificacionBimestralRepository calificacionBimestralRepository;

    public AlumnoCalificacionService(AlumnoService alumnoService,
                                     DetalleMatriculaRepository detalleMatriculaRepository,
                                     CalificacionBimestralRepository calificacionBimestralRepository) {
        this.alumnoService = alumnoService;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.calificacionBimestralRepository = calificacionBimestralRepository;
    }

    @Transactional(readOnly = true)
    public AlumnoPerfilDto obtenerPerfil(Integer idAlumno, String periodo) {
        var alumno = alumnoService.obtenerPorId(idAlumno);
        List<DetalleMatricula> detalles = obtenerDetallesAlumno(idAlumno, periodo);

        if (detalles.isEmpty()) {
            throw new RuntimeException("El alumno no tiene matrícula registrada para el período solicitado.");
        }

        DetalleMatricula detalle = detalles.get(0);
        Aula aula = detalle.getMatricula().getAula();
        Docente tutor = aula.getTutor();

        AlumnoPerfilDto dto = new AlumnoPerfilDto();
        dto.setIdAlumno(alumno.getIdAlumno());
        dto.setUsuarioId(alumno.getUsuario() != null ? alumno.getUsuario().getId() : null);
        dto.setCodigoEstudiante(alumno.getCodigoEstudiante());
        dto.setNombres(alumno.getNombres());
        dto.setApellidos(alumno.getApellidos());
        dto.setNivel(aula.getNivel());
        dto.setGrado(aula.getGrado());
        dto.setSeccion(aula.getSeccion());
        dto.setAulaId(aula.getIdAula());
        dto.setPeriodo(aula.getPeriodo());
        dto.setTutorNombres(tutor != null ? tutor.getNombres() : null);
        dto.setTutorApellidos(tutor != null ? tutor.getApellidos() : null);
        dto.setEstado(alumno.getEstado());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<AlumnoCursoNotasDto> listarCursosDelAlumno(Integer idAlumno, String periodo) {
        return obtenerDetallesAlumno(idAlumno, periodo).stream()
                .map(detalle -> mapDetalle(detalle, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlumnoCursoNotasDto> listarNotasDelAlumno(Integer idAlumno, String periodo) {
        return obtenerDetallesAlumno(idAlumno, periodo).stream()
                .map(detalle -> mapDetalle(detalle, true))
                .collect(Collectors.toList());
    }

    private List<DetalleMatricula> obtenerDetallesAlumno(Integer idAlumno, String periodo) {
        if (periodo == null || periodo.isBlank()) {
            return detalleMatriculaRepository.findByMatricula_Alumno_IdAlumno(idAlumno);
        }
        return detalleMatriculaRepository.findByMatricula_Alumno_IdAlumnoAndCursoProgramado_Aula_Periodo(idAlumno, periodo);
    }

    private AlumnoCursoNotasDto mapDetalle(DetalleMatricula detalle, boolean incluirNotas) {
        AlumnoCursoNotasDto dto = new AlumnoCursoNotasDto();
        dto.setIdDetalle(detalle.getIdDetalle());
        dto.setIdCursoProg(detalle.getCursoProgramado().getIdCursoProg());

        Materia materia = detalle.getCursoProgramado().getMateria();
        Docente docente = detalle.getCursoProgramado().getDocente();
        Aula aula = detalle.getCursoProgramado().getAula();

        dto.setMateria(materia != null ? materia.getNombre() : null);
        dto.setDocente(docente != null ? docente.getNombres() + " " + docente.getApellidos() : null);
        dto.setGrado(aula != null ? aula.getGrado() : null);
        dto.setSeccion(aula != null ? aula.getSeccion() : null);
        dto.setPeriodo(aula != null ? aula.getPeriodo() : null);
        dto.setEstadoCurso(detalle.getEstadoCurso());
        dto.setPromedioFinal(detalle.getPromedioFinal());
        dto.setLetraFinal(detalle.getLetraFinal());

        if (incluirNotas) {
            List<CalificacionBimestral> calificaciones = calificacionBimestralRepository
                    .findByDetalleMatricula_IdDetalleOrderByNumeroBimestreAsc(detalle.getIdDetalle());
            dto.setCalificacionesBimestrales(calificaciones.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setCalificacionesBimestrales(Collections.emptyList());
        }

        return dto;
    }

    private CalificacionResponseDto toDto(CalificacionBimestral calificacion) {
        CalificacionResponseDto dto = new CalificacionResponseDto();
        dto.setIdCalificacion(calificacion.getIdCalificacion());
        dto.setNumeroBimestre(calificacion.getNumeroBimestre());
        dto.setEv1(calificacion.getEv1());
        dto.setEv2(calificacion.getEv2());
        dto.setEv3(calificacion.getEv3());
        dto.setEvFinal(calificacion.getEvFinal());
        dto.setPromedioBimestre(calificacion.getPromedioBimestre());
        dto.setLetraBimestre(calificacion.getLetraBimestre());
        return dto;
    }
}
