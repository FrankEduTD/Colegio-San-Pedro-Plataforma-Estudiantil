package com.sanpedro.repository;

import com.sanpedro.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    // Validaciones para evitar duplicados críticos
    boolean existsByDni(String dni);
    boolean existsByCodigoEstudiante(String codigoEstudiante);

    // Validar que el usuario (cuenta) no esté siendo usado por otro alumno
    boolean existsByUsuarioIdUsuario(Integer usuarioId);

    // Filtro dinámico: Búsqueda por coincidencia en Código, DNI, nombres o apellidos Y estado
    @Query("SELECT a FROM Alumno a WHERE " +
            "(:termino IS NULL OR a.codigoEstudiante LIKE %:termino% OR a.dni LIKE %:termino% OR a.nombres LIKE %:termino% OR a.apellidos LIKE %:termino%) AND " +
            "(:estado IS NULL OR a.estado = :estado) " +
            "ORDER BY a.apellidos ASC")
    List<Alumno> filtrarAlumnos(@Param("termino") String termino, @Param("estado") String estado);
}