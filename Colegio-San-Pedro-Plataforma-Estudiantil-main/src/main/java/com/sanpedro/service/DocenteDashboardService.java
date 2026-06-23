package com.sanpedro.service;

import com.sanpedro.dto.DocenteDashboardDto;
import com.sanpedro.dto.DocenteCursoAulaDto;
import com.sanpedro.dto.CursoRosterAlumnoDto;
import com.sanpedro.dto.AlumnoCalificacionDto;
import com.sanpedro.dto.EdicionNotaDto;
import com.sanpedro.dto.CalificacionResponseDto;
import com.sanpedro.dto.HorarioDto;
import com.sanpedro.dto.DocenteTutoriaDto;
import com.sanpedro.dto.TutoriaApoderadoDto;
import com.sanpedro.model.Aula;
import com.sanpedro.model.CalificacionBimestral;
import com.sanpedro.model.CursoProgramado;
import com.sanpedro.model.DetalleMatricula;
import com.sanpedro.model.Docente;
import com.sanpedro.model.Horario;
import com.sanpedro.model.Matricula;
import com.sanpedro.repository.AulaRepository;
import com.sanpedro.repository.CalificacionBimestralRepository;
import com.sanpedro.repository.CursoProgramadoRepository;
import com.sanpedro.repository.DetalleMatriculaRepository;
import com.sanpedro.repository.HorarioRepository;
import com.sanpedro.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class DocenteDashboardService {

    private final DocenteService docenteService;
    private final CursoProgramadoRepository cursoProgramadoRepository;
    private final HorarioRepository horarioRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final CalificacionBimestralRepository calificacionBimestralRepository;
    private final CalificacionAdminService calificacionAdminService;
    private final AulaRepository aulaRepository;
    private final MatriculaRepository matriculaRepository;

    public DocenteDashboardService(DocenteService docenteService,
                                   CursoProgramadoRepository cursoProgramadoRepository,
                                   HorarioRepository horarioRepository,
                                   DetalleMatriculaRepository detalleMatriculaRepository,
                                   CalificacionBimestralRepository calificacionBimestralRepository,
                                   CalificacionAdminService calificacionAdminService,
                                   AulaRepository aulaRepository,
                                   MatriculaRepository matriculaRepository) {
        this.docenteService = docenteService;
        this.cursoProgramadoRepository = cursoProgramadoRepository;
        this.horarioRepository = horarioRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.calificacionBimestralRepository = calificacionBimestralRepository;
        this.calificacionAdminService = calificacionAdminService;
        this.aulaRepository = aulaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public DocenteDashboardDto obtenerDashboard(String username) {
        Docente docente = docenteService.obtenerPorUsername(username);
        List<CursoProgramado> cursosDocente = cursoProgramadoRepository.findByDocente_IdDocente(docente.getIdDocente());

        DocenteDashboardDto dto = new DocenteDashboardDto();
        dto.setDocenteNombre((docente.getNombres() + " " + docente.getApellidos()).trim());
        dto.setEspecialidad(docente.getEspecialidad());
        dto.setTotalAulasCargo(contarAulasUnicas(cursosDocente));
        dto.setClasesHoy(mapearClasesHoy(docente.getIdDocente()));
        dto.setAlertasAcademicas(generarAlertas(cursosDocente));
        return dto;
    }

    public List<DocenteCursoAulaDto> listarMisCursosAulas(String username) {
        Docente docente = docenteService.obtenerPorUsername(username);
        List<CursoProgramado> cursos = cursoProgramadoRepository.findByDocente_IdDocente(docente.getIdDocente());

        List<DocenteCursoAulaDto> resultado = new ArrayList<>();
        for (CursoProgramado curso : cursos) {
            DocenteCursoAulaDto dto = new DocenteCursoAulaDto();
            dto.setIdCursoProg(curso.getIdCursoProg());
            dto.setAulaId(curso.getAula() != null ? curso.getAula().getIdAula() : null);
            dto.setMateria(curso.getMateria() != null ? curso.getMateria().getNombre() : "Materia");
            dto.setGrado(curso.getAula() != null ? curso.getAula().getGrado() : "");
            dto.setSeccion(curso.getAula() != null ? curso.getAula().getSeccion() : "");
            dto.setPeriodo(curso.getAula() != null ? curso.getAula().getPeriodo() : "");
            resultado.add(dto);
        }
        return resultado;
    }

    public List<HorarioDto> listarMiHorarioSemanal(String username) {
        Docente docente = docenteService.obtenerPorUsername(username);
        List<Horario> horarios = horarioRepository.findByCursoProgramado_Docente_IdDocente(docente.getIdDocente());

        return horarios.stream()
                .filter(horario -> horario.getEstado() == null || !"inactivo".equalsIgnoreCase(horario.getEstado()))
                .sorted(comparadorHorarioSemanal())
                .map(this::mapearHorarioDto)
                .toList();
    }

    public DocenteTutoriaDto obtenerMiTutoria(String username) {
        Docente docente = docenteService.obtenerPorUsername(username);
        List<Aula> aulasTutor = aulaRepository.findByTutorIdDocente(docente.getIdDocente());

        if (aulasTutor.isEmpty()) {
            throw new RuntimeException("No tienes un salon asignado como tutor.");
        }

        Aula aula = aulasTutor.get(0);
        List<Matricula> matriculas = matriculaRepository.findByAula_IdAula(aula.getIdAula());

        DocenteTutoriaDto dto = new DocenteTutoriaDto();
        dto.setAulaId(aula.getIdAula());
        dto.setNivel(aula.getNivel());
        dto.setGrado(aula.getGrado());
        dto.setSeccion(aula.getSeccion());
        dto.setPeriodo(aula.getPeriodo());
        dto.setTotalAlumnos(matriculas.size());
        dto.setApoderados(mapearApoderados(matriculas));
        return dto;
    }

    public List<CursoRosterAlumnoDto> listarRosterDeCurso(String username, Integer idCursoProg) {
        Docente docente = docenteService.obtenerPorUsername(username);
        CursoProgramado curso = cursoProgramadoRepository.findById(idCursoProg)
                .orElseThrow(() -> new RuntimeException("Curso programado no encontrado."));

        if (curso.getDocente() == null || !docente.getIdDocente().equals(curso.getDocente().getIdDocente())) {
            throw new RuntimeException("No tienes permisos para ver alumnos de este curso.");
        }

        List<DetalleMatricula> detalles = detalleMatriculaRepository.findByCursoProgramado_IdCursoProg(idCursoProg);
        List<CursoRosterAlumnoDto> roster = new ArrayList<>();

        for (DetalleMatricula detalle : detalles) {
            CursoRosterAlumnoDto alumnoDto = new CursoRosterAlumnoDto();
            alumnoDto.setIdAlumno(detalle.getMatricula().getAlumno().getIdAlumno());
            alumnoDto.setCodigoEstudiante(detalle.getMatricula().getAlumno().getCodigoEstudiante());
            alumnoDto.setNombres(detalle.getMatricula().getAlumno().getNombres());
            alumnoDto.setApellidos(detalle.getMatricula().getAlumno().getApellidos());
            alumnoDto.setEstadoMatricula(detalle.getMatricula().getEstado());
            roster.add(alumnoDto);
        }

        return roster;
    }

    public List<AlumnoCalificacionDto> obtenerPlanillaCalificaciones(String username, Integer idCursoProg, Integer numeroBimestre) {
        validarPropiedadCurso(username, idCursoProg);
        if (numeroBimestre < 1 || numeroBimestre > 4) {
            throw new RuntimeException("El bimestre debe estar entre 1 y 4.");
        }
        return calificacionAdminService.obtenerPlanillaNotas(idCursoProg, numeroBimestre);
    }

    public CalificacionResponseDto guardarNotaDocente(String username,
                                                      Integer idDetalle,
                                                      Integer numeroBimestre,
                                                      EdicionNotaDto dto) {
        DetalleMatricula detalle = detalleMatriculaRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle de matrícula no encontrado."));

        Integer cursoId = detalle.getCursoProgramado().getIdCursoProg();
        validarPropiedadCurso(username, cursoId);

        CalificacionBimestral calificacion = calificacionAdminService.forzarEdicionNotas(idDetalle, numeroBimestre, dto);
        CalificacionResponseDto response = new CalificacionResponseDto();
        response.setIdCalificacion(calificacion.getIdCalificacion());
        response.setNumeroBimestre(calificacion.getNumeroBimestre());
        response.setEv1(calificacion.getEv1());
        response.setEv2(calificacion.getEv2());
        response.setEv3(calificacion.getEv3());
        response.setEvFinal(calificacion.getEvFinal());
        response.setPromedioBimestre(calificacion.getPromedioBimestre());
        response.setLetraBimestre(calificacion.getLetraBimestre());
        return response;
    }

    private void validarPropiedadCurso(String username, Integer idCursoProg) {
        Docente docente = docenteService.obtenerPorUsername(username);
        CursoProgramado curso = cursoProgramadoRepository.findById(idCursoProg)
                .orElseThrow(() -> new RuntimeException("Curso programado no encontrado."));

        if (curso.getDocente() == null || !docente.getIdDocente().equals(curso.getDocente().getIdDocente())) {
            throw new RuntimeException("No tienes permisos para gestionar este curso.");
        }
    }

    private Integer contarAulasUnicas(List<CursoProgramado> cursosDocente) {
        Set<Integer> aulas = new HashSet<>();
        for (CursoProgramado curso : cursosDocente) {
            if (curso.getAula() != null && curso.getAula().getIdAula() != null) {
                aulas.add(curso.getAula().getIdAula());
            }
        }
        return aulas.size();
    }

    private List<TutoriaApoderadoDto> mapearApoderados(List<Matricula> matriculas) {
        return matriculas.stream()
                .map(matricula -> {
                    TutoriaApoderadoDto dto = new TutoriaApoderadoDto();
                    dto.setIdAlumno(matricula.getAlumno().getIdAlumno());
                    dto.setCodigoEstudiante(matricula.getAlumno().getCodigoEstudiante());
                    dto.setNombres(matricula.getAlumno().getNombres());
                    dto.setApellidos(matricula.getAlumno().getApellidos());
                    dto.setTelefonoApoderado(matricula.getAlumno().getTelefonoApoderado());
                    dto.setEstadoMatricula(matricula.getEstado());
                    return dto;
                })
                .sorted(Comparator.comparing(TutoriaApoderadoDto::getApellidos, Comparator.nullsLast(String::compareTo)))
                .toList();
    }

    private HorarioDto mapearHorarioDto(Horario horario) {
        HorarioDto dto = new HorarioDto();
        dto.setIdHorario(horario.getIdHorario());
        dto.setCursoProgramadoId(horario.getCursoProgramado().getIdCursoProg());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setEstado(horario.getEstado());

        dto.setCursoNombre(horario.getCursoProgramado().getMateria().getNombre());
        dto.setDocenteNombre(horario.getCursoProgramado().getDocente().getNombres() + " " + horario.getCursoProgramado().getDocente().getApellidos());
        dto.setGrado(horario.getCursoProgramado().getAula().getGrado());
        dto.setSeccion(horario.getCursoProgramado().getAula().getSeccion());
        dto.setPeriodo(horario.getCursoProgramado().getAula().getPeriodo());
        return dto;
    }

    private Comparator<Horario> comparadorHorarioSemanal() {
        Map<String, Integer> orden = new HashMap<>();
        orden.put("lunes", 1);
        orden.put("martes", 2);
        orden.put("miercoles", 3);
        orden.put("miércoles", 3);
        orden.put("jueves", 4);
        orden.put("viernes", 5);

        return Comparator
                .comparingInt((Horario horario) -> orden.getOrDefault(normalizarDia(horario.getDiaSemana()), 99))
                .thenComparing(Horario::getHoraInicio, Comparator.nullsLast(String::compareTo));
    }

    private String normalizarDia(String diaSemana) {
        if (diaSemana == null) {
            return "";
        }

        return diaSemana.toLowerCase(Locale.ROOT).trim();
    }

    private List<DocenteDashboardDto.ClaseHoyDto> mapearClasesHoy(Integer idDocente) {
        String diaActual = diaSemanaEnEspanol(LocalDate.now().getDayOfWeek());
        List<Horario> horarios = horarioRepository
                .findByCursoProgramado_Docente_IdDocenteAndDiaSemanaIgnoreCaseOrderByHoraInicioAsc(idDocente, diaActual);

        List<DocenteDashboardDto.ClaseHoyDto> clases = new ArrayList<>();
        for (Horario horario : horarios) {
            CursoProgramado curso = horario.getCursoProgramado();
            DocenteDashboardDto.ClaseHoyDto claseDto = new DocenteDashboardDto.ClaseHoyDto();
            claseDto.setHoraInicio(horario.getHoraInicio());
            claseDto.setHoraFin(horario.getHoraFin());
            claseDto.setMateria(curso.getMateria() != null ? curso.getMateria().getNombre() : "Materia");
            claseDto.setGrado(curso.getAula() != null ? curso.getAula().getGrado() : "");
            claseDto.setSeccion(curso.getAula() != null ? curso.getAula().getSeccion() : "");
            clases.add(claseDto);
        }
        return clases;
    }

    private List<DocenteDashboardDto.AlertaAcademicaDto> generarAlertas(List<CursoProgramado> cursosDocente) {
        List<DocenteDashboardDto.AlertaAcademicaDto> alertas = new ArrayList<>();
        int bimestreActual = obtenerBimestreActual();

        for (CursoProgramado curso : cursosDocente) {
            if (faltanNotasBimestre(curso.getIdCursoProg(), bimestreActual)) {
                DocenteDashboardDto.AlertaAcademicaDto alerta = new DocenteDashboardDto.AlertaAcademicaDto();
                alerta.setTitulo("Registro de notas pendiente");
                alerta.setMensaje("Falta registrar notas del Bimestre " + bimestreActual + " en "
                        + curso.getAula().getGrado() + " " + curso.getAula().getSeccion()
                        + " - " + curso.getMateria().getNombre());
                alertas.add(alerta);
            }
        }

        DocenteDashboardDto.AlertaAcademicaDto periodoAbierto = new DocenteDashboardDto.AlertaAcademicaDto();
        periodoAbierto.setTitulo("Evaluaciones habilitadas");
        periodoAbierto.setMensaje("Periodo de evaluaciones abierto para el Bimestre " + bimestreActual + ".");
        alertas.add(periodoAbierto);

        return alertas;
    }

    private boolean faltanNotasBimestre(Integer cursoProgId, Integer bimestre) {
        List<DetalleMatricula> detalles = detalleMatriculaRepository.findByCursoProgramado_IdCursoProg(cursoProgId);
        for (DetalleMatricula detalle : detalles) {
            Optional<CalificacionBimestral> calificacion = calificacionBimestralRepository
                    .findByDetalleMatricula_IdDetalleAndNumeroBimestre(detalle.getIdDetalle(), bimestre);

            if (calificacion.isEmpty()) {
                return true;
            }

            CalificacionBimestral nota = calificacion.get();
            if (nota.getEv1() == null || nota.getEv2() == null || nota.getEv3() == null || nota.getEvFinal() == null) {
                return true;
            }
        }
        return false;
    }

    private Integer obtenerBimestreActual() {
        int mes = LocalDate.now().getMonthValue();
        if (mes <= 3) {
            return 1;
        }
        if (mes <= 6) {
            return 2;
        }
        if (mes <= 9) {
            return 3;
        }
        return 4;
    }

    private String diaSemanaEnEspanol(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
