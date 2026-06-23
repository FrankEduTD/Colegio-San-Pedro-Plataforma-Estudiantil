package com.sanpedro.repository;

import com.sanpedro.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    List<Matricula> findByAlumno_IdAlumno(Integer idAlumno);
    List<Matricula> findByAlumno_IdAlumnoAndAula_Periodo(Integer idAlumno, String periodo);
    List<Matricula> findByAula_IdAula(Integer aulaId);
    List<Matricula> findByAula_IdAulaAndEstado(Integer aulaId, String estado);
    List<Matricula> findByAlumno_IdAlumnoAndEstado(Integer idAlumno, String estado);
}