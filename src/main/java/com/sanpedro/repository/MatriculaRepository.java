package com.sanpedro.repository;

import com.sanpedro.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    // Por ahora no necesitamos métodos extra aquí.
    // Spring Boot ya nos da save(), findById(), findAll(), etc.
}