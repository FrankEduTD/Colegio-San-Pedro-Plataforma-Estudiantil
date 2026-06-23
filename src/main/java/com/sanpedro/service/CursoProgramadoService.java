package com.sanpedro.service;

import com.sanpedro.model.Aula;
import com.sanpedro.model.CursoProgramado;
import com.sanpedro.model.Docente;
import com.sanpedro.model.Materia;
import com.sanpedro.repository.AulaRepository;
import com.sanpedro.repository.CursoProgramadoRepository;
import com.sanpedro.repository.DocenteRepository;
import com.sanpedro.repository.MateriaRepository;
import com.sanpedro.dto.CursoProgramadoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoProgramadoService {

    private final CursoProgramadoRepository cursoProgRepository;
    private final MateriaRepository materiaRepository;
    private final DocenteRepository docenteRepository;
    private final AulaRepository aulaRepository;

    public CursoProgramadoService(CursoProgramadoRepository cursoProgRepository,
                                  MateriaRepository materiaRepository,
                                  DocenteRepository docenteRepository,
                                  AulaRepository aulaRepository) {
        this.cursoProgRepository = cursoProgRepository;
        this.materiaRepository = materiaRepository;
        this.docenteRepository = docenteRepository;
        this.aulaRepository = aulaRepository;
    }

    public List<CursoProgramado> listarTodos() {
        return cursoProgRepository.findAll();
    }

    public List<CursoProgramado> listarPorAula(Integer aulaId) {
        return cursoProgRepository.findByAulaIdAula(aulaId);
    }

    public CursoProgramado obtenerPorId(Integer id) {
        return cursoProgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programación no encontrada con ID: " + id));
    }

    public CursoProgramado guardar(CursoProgramadoDto dto) {
        // 1. Validar que las 3 entidades existan
        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new RuntimeException("La materia seleccionada no existe."));
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new RuntimeException("El docente seleccionado no existe."));
        Aula aula = aulaRepository.findById(dto.getAulaId())
                .orElseThrow(() -> new RuntimeException("El aula seleccionada no existe."));

        CursoProgramado programacion;

        if (dto.getIdCursoProg() == null) {
            // === CREACIÓN ===
            if (cursoProgRepository.existsByMateriaIdMateriaAndAulaIdAula(materia.getIdMateria(), aula.getIdAula())) {
                throw new RuntimeException("Error: El aula ya tiene asignado un profesor para esta materia.");
            }
            programacion = new CursoProgramado();
            programacion.setEstado("Activo");
        } else {
            // === ACTUALIZACIÓN ===
            programacion = obtenerPorId(dto.getIdCursoProg());

            // Si cambian la materia o el aula, validamos que no choque con otra programación
            boolean cambioMateriaOAula = !programacion.getMateria().getIdMateria().equals(materia.getIdMateria()) ||
                    !programacion.getAula().getIdAula().equals(aula.getIdAula());

            if (cambioMateriaOAula && cursoProgRepository.existsByMateriaIdMateriaAndAulaIdAula(materia.getIdMateria(), aula.getIdAula())) {
                throw new RuntimeException("Error: El aula ya tiene esta materia programada con otro registro.");
            }
            programacion.setEstado(dto.getEstado() != null ? dto.getEstado() : programacion.getEstado());
        }

        // 2. Asignar las relaciones y guardar
        programacion.setMateria(materia);
        programacion.setDocente(docente);
        programacion.setAula(aula);

        return cursoProgRepository.save(programacion);
    }

    public void eliminar(Integer id) {
        CursoProgramado programacion = obtenerPorId(id);
        programacion.setEstado("Cerrado");
        cursoProgRepository.save(programacion);
    }
}