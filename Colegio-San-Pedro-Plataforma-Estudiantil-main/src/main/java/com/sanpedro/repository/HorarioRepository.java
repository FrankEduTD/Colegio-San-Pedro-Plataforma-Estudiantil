package com.sanpedro.repository;

import com.sanpedro.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    List<Horario> findByCursoProgramado_Aula_IdAula(Integer aulaId);
    List<Horario> findByDiaSemanaIgnoreCase(String diaSemana);
    List<Horario> findByCursoProgramado_Docente_IdDocenteAndDiaSemanaIgnoreCaseOrderByHoraInicioAsc(Integer idDocente, String diaSemana);
    List<Horario> findByCursoProgramado_Docente_IdDocente(Integer idDocente);
}
