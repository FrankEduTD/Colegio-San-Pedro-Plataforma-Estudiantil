package com.sanpedro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AlumnoDto {

	private Integer idAlumno;

	private Integer usuarioId;

	@NotBlank(message = "El DNI es obligatorio")
	@Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe contener exactamente 8 números")
	private String dni;

	@Size(max = 15, message = "El código de estudiante no puede exceder los 15 caracteres")
	private String codigoEstudiante;

	@NotBlank(message = "Los nombres son obligatorios")
	@Size(max = 80, message = "Los nombres no pueden exceder los 80 caracteres")
	private String nombres;

	@NotBlank(message = "Los apellidos son obligatorios")
	@Size(max = 80, message = "Los apellidos no pueden exceder los 80 caracteres")
	private String apellidos;

	@NotNull(message = "La fecha de nacimiento es obligatoria")
	private LocalDate fechaNacimiento;

	@Size(max = 15, message = "El teléfono del apoderado no puede exceder los 15 caracteres")
	private String telefonoApoderado;

	private String estado;

	public AlumnoDto() {
	}

	// Getters y Setters
	public Integer getIdAlumno() { return idAlumno; }
	public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }

	public Integer getUsuarioId() { return usuarioId; }
	public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

	public String getDni() { return dni; }
	public void setDni(String dni) { this.dni = dni; }

	public String getCodigoEstudiante() { return codigoEstudiante; }
	public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }

	public String getNombres() { return nombres; }
	public void setNombres(String nombres) { this.nombres = nombres; }

	public String getApellidos() { return apellidos; }
	public void setApellidos(String apellidos) { this.apellidos = apellidos; }

	public LocalDate getFechaNacimiento() { return fechaNacimiento; }
	public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

	public String getTelefonoApoderado() { return telefonoApoderado; }
	public void setTelefonoApoderado(String telefonoApoderado) { this.telefonoApoderado = telefonoApoderado; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }
}