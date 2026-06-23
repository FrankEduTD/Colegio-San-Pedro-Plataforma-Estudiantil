package com.sanpedro.dto;

import java.util.ArrayList;
import java.util.List;

public class DocenteDashboardDto {

    private String docenteNombre;
    private String especialidad;
    private Integer totalAulasCargo;
    private List<ClaseHoyDto> clasesHoy = new ArrayList<>();
    private List<AlertaAcademicaDto> alertasAcademicas = new ArrayList<>();

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getTotalAulasCargo() {
        return totalAulasCargo;
    }

    public void setTotalAulasCargo(Integer totalAulasCargo) {
        this.totalAulasCargo = totalAulasCargo;
    }

    public List<ClaseHoyDto> getClasesHoy() {
        return clasesHoy;
    }

    public void setClasesHoy(List<ClaseHoyDto> clasesHoy) {
        this.clasesHoy = clasesHoy;
    }

    public List<AlertaAcademicaDto> getAlertasAcademicas() {
        return alertasAcademicas;
    }

    public void setAlertasAcademicas(List<AlertaAcademicaDto> alertasAcademicas) {
        this.alertasAcademicas = alertasAcademicas;
    }

    public static class ClaseHoyDto {
        private String horaInicio;
        private String horaFin;
        private String materia;
        private String grado;
        private String seccion;

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
    }

    public static class AlertaAcademicaDto {
        private String titulo;
        private String mensaje;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
