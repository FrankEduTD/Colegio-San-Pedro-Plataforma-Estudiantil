package com.sanpedro.repository;

import com.sanpedro.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {

    // Evitar que se cree dos veces la misma aula en el mismo año (Ej: Dos "1ro A" en 2026)
    boolean existsByGradoAndSeccionAndPeriodo(String grado, String seccion, String periodo);

    // Listar aulas por año escolar (Ej: Mostrar solo las aulas de 2026)
    List<Aula> findByPeriodo(String periodo);

    // Opcional: Listar todas las aulas donde un profesor específico es tutor
    List<Aula> findByTutorIdDocente(Integer tutorId);
}