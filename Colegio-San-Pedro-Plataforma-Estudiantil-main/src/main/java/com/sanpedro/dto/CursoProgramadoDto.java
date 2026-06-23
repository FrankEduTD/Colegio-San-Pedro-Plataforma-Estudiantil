package com.sanpedro.dto;

import jakarta.validation.constraints.NotNull;

public class CursoProgramadoDto {

    private Integer idCursoProg;

    @NotNull(message = "Debe seleccionar una materia")
    private Integer materiaId;

    @NotNull(message = "Debe seleccionar un docente")
    private Integer docenteId;

    @NotNull(message = "Debe seleccionar un aula")
    private Integer aulaId;

    private String estado;

    public CursoProgramadoDto() {
    }

    public Integer getIdCursoProg() { return idCursoProg; }
    public void setIdCursoProg(Integer idCursoProg) { this.idCursoProg = idCursoProg; }

    public Integer getMateriaId() { return materiaId; }
    public void setMateriaId(Integer materiaId) { this.materiaId = materiaId; }

    public Integer getDocenteId() { return docenteId; }
    public void setDocenteId(Integer docenteId) { this.docenteId = docenteId; }

    public Integer getAulaId() { return aulaId; }
    public void setAulaId(Integer aulaId) { this.aulaId = aulaId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}