package com.sanpedro.repository;

import com.sanpedro.model.DetalleMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula, Integer> {

    // Método necesario para listar a los alumnos de un curso específico
    List<DetalleMatricula> findByCursoProgramado_IdCursoProg(Integer idCursoProg);
}