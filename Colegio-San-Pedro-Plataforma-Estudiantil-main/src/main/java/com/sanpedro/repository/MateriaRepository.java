package com.sanpedro.repository;

import com.sanpedro.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);

    List<Materia> findByAreaIgnoreCase(String area);
}