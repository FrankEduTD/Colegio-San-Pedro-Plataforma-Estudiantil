package com.sanpedro.repository;

import com.sanpedro.model.PagoPension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoPensionRepository extends JpaRepository<PagoPension, Integer> {
    List<PagoPension> findByMatricula_IdMatricula(Integer idMatricula);
}