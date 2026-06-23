package com.sanpedro.service;

import com.sanpedro.model.PagoPension;
import com.sanpedro.model.Alumno;
import com.sanpedro.model.Matricula;
import com.sanpedro.repository.MatriculaRepository;
import com.sanpedro.repository.PagoPensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import com.sanpedro.service.AlumnoService;

import java.time.LocalDate;
import java.util.List;

@Service
public class TesoreriaService {

    @Autowired
    private PagoPensionRepository pagoPensionRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlumnoService alumnoService;

    public List<PagoPension> obtenerPagosPorMatricula(Integer idMatricula) {
        return pagoPensionRepository.findByMatricula_IdMatricula(idMatricula);
    }

        public List<PagoPension> obtenerEstadoCuentaMiAlumno(String username) {
        Alumno alumno = alumnoService.obtenerPorUsername(username);

        List<Matricula> matriculasRegulares = matriculaRepository
            .findByAlumno_IdAlumnoAndEstado(alumno.getIdAlumno(), "Regular");

        List<Matricula> fuente = !matriculasRegulares.isEmpty()
            ? matriculasRegulares
            : matriculaRepository.findByAlumno_IdAlumno(alumno.getIdAlumno());

        Optional<Matricula> matriculaVigente = fuente.stream()
            .max(Comparator.comparing(Matricula::getFechaMatricula));

        if (matriculaVigente.isEmpty()) {
            throw new RuntimeException("No se encontro matricula activa para el alumno.");
        }

        return pagoPensionRepository.findByMatricula_IdMatricula(matriculaVigente.get().getIdMatricula())
            .stream()
            .sorted(Comparator.comparing(PagoPension::getFechaVencimiento, Comparator.nullsLast(LocalDate::compareTo)))
            .collect(Collectors.toList());
        }

    @Transactional
    public PagoPension procesarPago(Integer idPago, String metodoPago) {
        PagoPension pago = pagoPensionRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Recibo de pago no encontrado con el ID: " + idPago));

        if ("Pagado".equals(pago.getEstado())) {
            throw new RuntimeException("Operación rechazada: Este recibo ya se encuentra pagado.");
        }

        pago.setEstado("Pagado");
        pago.setFechaPago(LocalDate.now());
        pago.setMetodoPago(metodoPago);

        return pagoPensionRepository.save(pago);
    }
}