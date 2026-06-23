package com.sanpedro.dto;

import java.util.ArrayList;
import java.util.List;

public class DocenteTutoriaDto {

    private Integer aulaId;
    private String nivel;
    private String grado;
    private String seccion;
    private String periodo;
    private Integer totalAlumnos;
    private List<TutoriaApoderadoDto> apoderados = new ArrayList<>();

    public Integer getAulaId() {
        return aulaId;
    }

    public void setAulaId(Integer aulaId) {
        this.aulaId = aulaId;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
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

    public Integer getTotalAlumnos() {
        return totalAlumnos;
    }

    public void setTotalAlumnos(Integer totalAlumnos) {
        this.totalAlumnos = totalAlumnos;
    }

    public List<TutoriaApoderadoDto> getApoderados() {
        return apoderados;
    }

    public void setApoderados(List<TutoriaApoderadoDto> apoderados) {
        this.apoderados = apoderados;
    }
}
