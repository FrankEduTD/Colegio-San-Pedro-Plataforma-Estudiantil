package com.sanpedro.service;

import com.sanpedro.model.Materia;
import com.sanpedro.repository.MateriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;

    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public List<Materia> listarTodas() {
        return materiaRepository.findAll();
    }

    public Materia obtenerPorId(Integer id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada con ID: " + id));
    }

    public Materia guardar(Materia materia) {
        if (materia.getIdMateria() == null) {
            if (materiaRepository.existsByNombreIgnoreCase(materia.getNombre())) {
                throw new RuntimeException("Error: Ya existe un curso registrado con el nombre '" + materia.getNombre() + "'.");
            }
        } else {
            // Lógica de Actualización
            Materia existente = obtenerPorId(materia.getIdMateria());

            if (!existente.getNombre().equalsIgnoreCase(materia.getNombre())
                    && materiaRepository.existsByNombreIgnoreCase(materia.getNombre())) {
                throw new RuntimeException("Error: El nombre propuesto ya le pertenece a otro curso.");
            }
        }

        return materiaRepository.save(materia);
    }

    public void eliminar(Integer id) {
        obtenerPorId(id);
        try {
            materiaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se puede eliminar la materia porque ya está siendo utilizada en la programación de aulas.");
        }
    }
}