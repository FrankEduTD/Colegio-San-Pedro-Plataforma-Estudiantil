package com.sanpedro.service;

import com.sanpedro.model.Aula;
import com.sanpedro.model.Docente;
import com.sanpedro.repository.AulaRepository;
import com.sanpedro.repository.DocenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final DocenteRepository docenteRepository;

    public AulaService(AulaRepository aulaRepository, DocenteRepository docenteRepository) {
        this.aulaRepository = aulaRepository;
        this.docenteRepository = docenteRepository;
    }

    public List<Aula> listarTodas() {
        return aulaRepository.findAll();
    }

    public List<Aula> listarPorPeriodo(String periodo) {
        return aulaRepository.findByPeriodo(periodo);
    }

    public Aula obtenerPorId(Integer id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada con ID: " + id));
    }

    public Aula guardar(Aula aula, Integer tutorId) {
        // 1. Validar que el profesor asignado como tutor exista en el sistema
        Docente tutor = docenteRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("El docente seleccionado para tutoría no existe."));

        if (aula.getIdAula() == null) {
            // === LÓGICA DE CREACIÓN ===
            // Evitar duplicados exactos en el mismo periodo (Ej: que no existan dos "3ro A" en 2026)
            if (aulaRepository.existsByGradoAndSeccionAndPeriodo(aula.getGrado(), aula.getSeccion(), aula.getPeriodo())) {
                throw new RuntimeException("Error: El aula de " + aula.getGrado() + " sezoón '" + aula.getSeccion() + "' ya está programada para el periodo " + aula.getPeriodo() + ".");
            }
        } else {
            // === LÓGICA DE ACTUALIZACIÓN ===
            Aula existente = obtenerPorId(aula.getIdAula());

            boolean alteroIdentificadores = !existente.getGrado().equals(aula.getGrado()) ||
                    !existente.getSeccion().equals(aula.getSeccion()) ||
                    !existente.getPeriodo().equals(aula.getPeriodo());

            if (alteroIdentificadores && aulaRepository.existsByGradoAndSeccionAndPeriodo(aula.getGrado(), aula.getSeccion(), aula.getPeriodo())) {
                throw new RuntimeException("Error: Los cambios entran en conflicto con un aula ya registrada para el periodo " + aula.getPeriodo() + ".");
            }
        }

        // 2. Vincular el tutor y guardar
        aula.setTutor(tutor);
        return aulaRepository.save(aula);
    }

    public void eliminar(Integer id) {
        obtenerPorId(id);
        try {
            aulaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se puede eliminar el aula porque ya cuenta con alumnos matriculados o cursos programados.");
        }
    }
}