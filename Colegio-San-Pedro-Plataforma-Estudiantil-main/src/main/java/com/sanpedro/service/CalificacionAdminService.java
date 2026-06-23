package com.sanpedro.service;

import com.sanpedro.dto.EdicionNotaDto;
import com.sanpedro.dto.AlumnoCalificacionDto;
import com.sanpedro.model.CalificacionBimestral;
import com.sanpedro.model.DetalleMatricula;
import com.sanpedro.repository.CalificacionBimestralRepository;
import com.sanpedro.repository.DetalleMatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalificacionAdminService {

    @Autowired
    private CalificacionBimestralRepository calificacionRepository;

    @Autowired
    private DetalleMatriculaRepository detalleMatriculaRepository;

    @Transactional(readOnly = true)
    public List<AlumnoCalificacionDto> obtenerPlanillaNotas(Integer cursoProgId, Integer numeroBimestre) {

        // 1. Obtenemos todos los detalles de matrícula (alumnos) inscritos en este curso programado
        List<DetalleMatricula> listaDetalles = detalleMatriculaRepository.findByCursoProgramado_IdCursoProg(cursoProgId);

        // 2. Transmutamos la lista relacional a la estructura plana que requiere la vista
        return listaDetalles.stream().map(detalle -> {
            AlumnoCalificacionDto dto = new AlumnoCalificacionDto();
            dto.setIdDetalle(detalle.getIdDetalle());
            dto.setAlumnoNombres(detalle.getMatricula().getAlumno().getNombres());
            dto.setAlumnoApellidos(detalle.getMatricula().getAlumno().getApellidos());

            // 3. Cruzamos con la tabla de calificaciones bimestrales
            Optional<CalificacionBimestral> calificacionOpt = calificacionRepository
                    .findByDetalleMatricula_IdDetalleAndNumeroBimestre(detalle.getIdDetalle(), numeroBimestre);

            // 4. Si el docente o el sistema ya registraron notas, las mapeamos; si no, viajan como null
            if (calificacionOpt.isPresent()) {
                CalificacionBimestral c = calificacionOpt.get();
                dto.setEv1(c.getEv1());
                dto.setEv2(c.getEv2());
                dto.setEv3(c.getEv3());
                dto.setEvFinal(c.getEvFinal());
                dto.setPromedioBimestre(c.getPromedioBimestre());
                dto.setLetraBimestre(c.getLetraBimestre());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    // Forzar la edición segura recalculando promedios

    @Transactional
    public CalificacionBimestral forzarEdicionNotas(Integer idDetalle, Integer numeroBimestre, EdicionNotaDto dto) {

        // 1. Validar que la inscripción al curso exista
        DetalleMatricula detalle = detalleMatriculaRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Inscripción de curso no encontrada."));

        if (numeroBimestre < 1 || numeroBimestre > 4) {
            throw new RuntimeException("El número de bimestre debe estar entre 1 y 4.");
        }

        // 2. Buscar si ya existe un registro de notas para ese bimestre, si no, crear uno nuevo
        CalificacionBimestral calificacion = calificacionRepository
                .findByDetalleMatricula_IdDetalleAndNumeroBimestre(idDetalle, numeroBimestre)
                .orElse(new CalificacionBimestral());

        if (calificacion.getIdCalificacion() == null) {
            calificacion.setDetalleMatricula(detalle);
            calificacion.setNumeroBimestre(numeroBimestre);
        }

        // 3. Asignar las notas numéricas enviadas por el admin
        calificacion.setEv1(dto.getEv1());
        calificacion.setEv2(dto.getEv2());
        calificacion.setEv3(dto.getEv3());
        calificacion.setEvFinal(dto.getEvFinal());

        // 4. Calcular el Promedio (Si todas las notas están ingresadas)
        if (dto.getEv1() != null && dto.getEv2() != null && dto.getEv3() != null && dto.getEvFinal() != null) {
            BigDecimal suma = dto.getEv1().add(dto.getEv2()).add(dto.getEv3()).add(dto.getEvFinal());
            BigDecimal promedio = suma.divide(new BigDecimal("4"), 0, RoundingMode.HALF_UP); // Redondeo a favor del alumno

            calificacion.setPromedioBimestre(promedio);
            calificacion.setLetraBimestre(calcularLetraMinedu(promedio.intValue()));
        }

        // 5. Guardar los cambios
        return calificacionRepository.save(calificacion);
    }

    // Metodo privado para la conversión a la escala literal del MINEDU
    private String calcularLetraMinedu(int promedio) {
        if (promedio >= 18 && promedio <= 20) return "AD";
        if (promedio >= 14 && promedio <= 17) return "A";
        if (promedio >= 11 && promedio <= 13) return "B";
        return "C"; // De 0 a 10
    }
}