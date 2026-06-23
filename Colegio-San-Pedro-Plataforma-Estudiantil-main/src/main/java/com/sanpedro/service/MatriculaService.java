package com.sanpedro.service;

import com.sanpedro.dto.MatriculaRequestDto;
import com.sanpedro.model.*;
import com.sanpedro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private DetalleMatriculaRepository detalleMatriculaRepository;

    @Autowired
    private CursoProgramadoRepository cursoProgramadoRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private PagoPensionRepository pagoPensionRepository;

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    @Transactional
    public Matricula registrarMatricula(MatriculaRequestDto request) {

        // 1. Validar que el alumno y el aula existan
        Alumno alumno = alumnoRepository.findById(request.getAlumnoId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        Aula aula = aulaRepository.findById(request.getAulaId())
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));

        // 2. Crear la cabecera de la Matrícula
        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setAula(aula);
        matricula.setFechaMatricula(request.getFechaMatricula());
        matricula.setEstado("Regular"); // Forzamos el estado para que no retorne null en el JSON

        Matricula matriculaGuardada = matriculaRepository.save(matricula);

        // 3. Buscar los cursos programados para esa aula (Usando el método corregido sin guion bajo)
        List<CursoProgramado> cursosDelAula = cursoProgramadoRepository.findByAulaIdAula(aula.getIdAula());

        if (cursosDelAula.isEmpty()) {
            throw new RuntimeException("El aula no tiene cursos programados. No se puede matricular.");
        }

        // 4. Inscribir automáticamente al alumno en todos los cursos del aula
        for (CursoProgramado curso : cursosDelAula) {
            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setMatricula(matriculaGuardada);
            detalle.setCursoProgramado(curso);
            detalle.setEstadoCurso("En curso"); // Forzamos el estado por defecto

            detalleMatriculaRepository.save(detalle);
        }

        // 5. Generación Automática del Cronograma de Pagos (Marzo a Diciembre)
        BigDecimal montoPensionBase = new BigDecimal("350.00"); // Monto fijo de la pensión
        int anioMatricula = request.getFechaMatricula().getYear();

        String[] meses = {"", "", "", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

        for (int mes = 3; mes <= 12; mes++) {
            PagoPension pension = new PagoPension();
            pension.setMatricula(matriculaGuardada);
            pension.setConcepto("Pensión " + meses[mes] + " " + anioMatricula);
            pension.setMonto(montoPensionBase);

            LocalDate vencimiento = LocalDate.of(anioMatricula, mes, 1).plusMonths(1).minusDays(1);
            pension.setFechaVencimiento(vencimiento);
            pension.setEstado("Pendiente");

            pagoPensionRepository.save(pension);
        }

        return matriculaGuardada;
    }
}