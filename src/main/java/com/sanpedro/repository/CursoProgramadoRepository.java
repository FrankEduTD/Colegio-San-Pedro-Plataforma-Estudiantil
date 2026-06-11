package com.sanpedro.repository;

import com.sanpedro.model.CursoProgramado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoProgramadoRepository extends JpaRepository<CursoProgramado, Integer> {

    // Validar que un aula no tenga la misma materia asignada dos veces
    boolean existsByMateriaIdMateriaAndAulaIdAula(Integer materiaId, Integer aulaId);

    // Listar todos los cursos programados para un aula específica (Útil para el frontend)
    List<CursoProgramado> findByAulaIdAula(Integer aulaId);
}