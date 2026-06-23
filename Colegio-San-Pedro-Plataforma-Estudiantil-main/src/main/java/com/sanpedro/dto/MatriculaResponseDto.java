package com.sanpedro.dto;

import java.time.LocalDate;

public class MatriculaResponseDto {
    private Integer idMatricula;
    private String alumnoNombres;
    private String alumnoApellidos;
    private String aulaNivel;
    private String aulaGrado;
    private String aulaSeccion;
    private LocalDate fechaMatricula;
    private String estado;

    // Getters y Setters
    public Integer getIdMatricula() { return idMatricula; }
    public void setIdMatricula(Integer idMatricula) { this.idMatricula = idMatricula; }
    public String getAlumnoNombres() { return alumnoNombres; }
    public void setAlumnoNombres(String alumnoNombres) { this.alumnoNombres = alumnoNombres; }
    public String getAlumnoApellidos() { return alumnoApellidos; }
    public void setAlumnoApellidos(String alumnoApellidos) { this.alumnoApellidos = alumnoApellidos; }
    public String getAulaNivel() { return aulaNivel; }
    public void setAulaNivel(String aulaNivel) { this.aulaNivel = aulaNivel; }
    public String getAulaGrado() { return aulaGrado; }
    public void setAulaGrado(String aulaGrado) { this.aulaGrado = aulaGrado; }
    public String getAulaSeccion() { return aulaSeccion; }
    public void setAulaSeccion(String aulaSeccion) { this.aulaSeccion = aulaSeccion; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}