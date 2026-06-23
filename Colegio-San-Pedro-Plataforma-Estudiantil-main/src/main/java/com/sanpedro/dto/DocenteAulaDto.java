package com.sanpedro.dto;

public class DocenteAulaDto {

    private Integer idDocente;
    private String nombres;
    private String apellidos;
    private String materia;
    private String email;
    private String telefono;

    public DocenteAulaDto() {
    }

    public DocenteAulaDto(Integer idDocente, String nombres, String apellidos, String materia, String email, String telefono) {
        this.idDocente = idDocente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.materia = materia;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Integer getIdDocente() { return idDocente; }
    public void setIdDocente(Integer idDocente) { this.idDocente = idDocente; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
