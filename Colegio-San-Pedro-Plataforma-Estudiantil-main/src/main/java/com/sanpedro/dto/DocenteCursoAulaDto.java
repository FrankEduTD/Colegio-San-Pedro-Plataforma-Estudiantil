package com.sanpedro.dto;

public class DocenteCursoAulaDto {

    private Integer idCursoProg;
    private Integer aulaId;
    private String materia;
    private String grado;
    private String seccion;
    private String periodo;

    public Integer getIdCursoProg() {
        return idCursoProg;
    }

    public void setIdCursoProg(Integer idCursoProg) {
        this.idCursoProg = idCursoProg;
    }

    public Integer getAulaId() {
        return aulaId;
    }

    public void setAulaId(Integer aulaId) {
        this.aulaId = aulaId;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
