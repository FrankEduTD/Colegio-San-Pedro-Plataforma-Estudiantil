package com.sanpedro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AulaDto {

    private Integer idAula;

    @NotBlank(message = "El nivel es obligatorio")
    @Size(max = 20, message = "El nivel no puede exceder los 20 caracteres")
    private String nivel;

    @NotBlank(message = "El grado es obligatorio")
    @Size(max = 20, message = "El grado no puede exceder los 20 caracteres")
    private String grado;

    @NotBlank(message = "La sección es obligatoria")
    @Size(max = 10, message = "La sección no puede exceder los 10 caracteres")
    private String seccion;

    @NotNull(message = "El ID del tutor es obligatorio")
    private Integer tutorId;

    @NotBlank(message = "El periodo es obligatorio")
    @Size(max = 10, message = "El periodo no puede exceder los 10 caracteres")
    private String periodo;

    @Min(value = 1, message = "La capacidad mínima debe ser 1")
    private Integer capacidad;

    public AulaDto() {
    }

    // Getters y Setters
    public Integer getIdAula() { return idAula; }
    public void setIdAula(Integer idAula) { this.idAula = idAula; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }

    public Integer getTutorId() { return tutorId; }
    public void setTutorId(Integer tutorId) { this.tutorId = tutorId; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
}