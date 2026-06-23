package com.sanpedro.dto;

import java.time.LocalDate;

public class MatriculaRequestDto {
    private Integer alumnoId;
    private Integer aulaId;
    private LocalDate fechaMatricula;

    // Getters y Setters
    public Integer getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Integer alumnoId) { this.alumnoId = alumnoId; }
    public Integer getAulaId() { return aulaId; }
    public void setAulaId(Integer aulaId) { this.aulaId = aulaId; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
}