package com.sanpedro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HorarioDto {

    private Integer idHorario;

    @NotNull(message = "Debe seleccionar un curso programado")
    private Integer cursoProgramadoId;

    @NotBlank(message = "Debe indicar el día de la semana")
    private String diaSemana;

    @NotBlank(message = "Debe indicar la hora de inicio")
    private String horaInicio;

    @NotBlank(message = "Debe indicar la hora de fin")
    private String horaFin;

    private String cursoNombre;
    private String docenteNombre;
    private String grado;
    private String seccion;
    private String periodo;
    private String estado;

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getCursoProgramadoId() {
        return cursoProgramadoId;
    }

    public void setCursoProgramadoId(Integer cursoProgramadoId) {
        this.cursoProgramadoId = cursoProgramadoId;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getCursoNombre() {
        return cursoNombre;
    }

    public void setCursoNombre(String cursoNombre) {
        this.cursoNombre = cursoNombre;
    }

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
