package com.sanpedro.repository;

import com.sanpedro.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    // Validar si el DNI ya existe (para evitar duplicados al registrar)
    boolean existsByDni(String dni);

    // Validar si un usuario ya tiene asignado un perfil de docente
    boolean existsByUsuarioIdUsuario(Integer usuarioId);

    // Filtro dinámico: Búsqueda por coincidencia en DNI, nombres o apellidos Y estado
    @Query("SELECT d FROM Docente d WHERE " +
            "(:termino IS NULL OR d.dni LIKE %:termino% OR d.nombres LIKE %:termino% OR d.apellidos LIKE %:termino%) AND " +
            "(:estado IS NULL OR d.estado = :estado) " +
            "ORDER BY d.apellidos ASC")
    List<Docente> filtrarDocentes(@Param("termino") String termino, @Param("estado") String estado);

    Optional<Docente> findByUsuario_Username(String username);
}