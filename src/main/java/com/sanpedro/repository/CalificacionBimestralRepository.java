package com.sanpedro.repository;

import com.sanpedro.model.CalificacionBimestral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalificacionBimestralRepository extends JpaRepository<CalificacionBimestral, Integer> {
    // Para buscar una nota específica sabiendo el id del curso matriculado y el bimestre
    Optional<CalificacionBimestral> findByDetalleMatricula_IdDetalleAndNumeroBimestre(Integer idDetalle, Integer numeroBimestre);
}