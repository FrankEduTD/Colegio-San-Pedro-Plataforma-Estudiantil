package com.sanpedro.repository;

import com.sanpedro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // 1. Busca un único usuario por su nombre de usuario (Vital para el Login)
    Optional<Usuario> findByUsername(String username);

    // 2. Busca una lista de usuarios por su rol
    List<Usuario> findByRol(String rol);

    // 3. Verifica si un username ya existe (Evita errores al crear usuarios)
    boolean existsByUsername(String username);

    // 4. Verifica si un email ya existe (Evita errores al crear usuarios)
    boolean existsByEmail(String email);

    // 5. Filtro dinámico: Busca por coincidencia en username/email Y por estado.
    @Query("SELECT u FROM Usuario u WHERE " +
            "(:termino IS NULL OR u.username LIKE %:termino% OR u.email LIKE %:termino%) AND " +
            "(:estado IS NULL OR u.estado = :estado) " +
            "ORDER BY u.fechaCreacion DESC")
    List<Usuario> filtrarUsuarios(@Param("termino") String termino, @Param("estado") String estado);
}