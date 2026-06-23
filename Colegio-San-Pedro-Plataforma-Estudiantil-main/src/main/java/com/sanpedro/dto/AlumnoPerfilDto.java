package com.sanpedro.dto;

public class AlumnoPerfilDto {
    private Integer idAlumno;
    private Integer usuarioId;
    private String codigoEstudiante;
    private String nombres;
    private String apellidos;
    private String nivel;
    private String grado;
    private String seccion;
    private Integer aulaId;
    private String periodo;
    private String tutorNombres;
    private String tutorApellidos;
    private String estado;

    public Integer getIdAlumno() { return idAlumno; }
    public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }
    public Integer getAulaId() { return aulaId; }
    public void setAulaId(Integer aulaId) { this.aulaId = aulaId; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getTutorNombres() { return tutorNombres; }
    public void setTutorNombres(String tutorNombres) { this.tutorNombres = tutorNombres; }
    public String getTutorApellidos() { return tutorApellidos; }
    public void setTutorApellidos(String tutorApellidos) { this.tutorApellidos = tutorApellidos; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
