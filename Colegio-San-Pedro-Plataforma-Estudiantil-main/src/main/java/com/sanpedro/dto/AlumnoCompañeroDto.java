package com.sanpedro.dto;

public class AlumnoCompañeroDto {

    private Integer idAlumno;
    private String nombres;
    private String apellidos;
    private String codigoEstudiante;
    private String dni;

    public AlumnoCompañeroDto() {
    }

    public AlumnoCompañeroDto(Integer idAlumno, String nombres, String apellidos, String codigoEstudiante, String dni) {
        this.idAlumno = idAlumno;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.codigoEstudiante = codigoEstudiante;
        this.dni = dni;
    }

    // Getters y Setters
    public Integer getIdAlumno() { return idAlumno; }
    public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
}
