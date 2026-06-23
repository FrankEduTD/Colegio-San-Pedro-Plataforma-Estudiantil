package com.sanpedro.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alumnos")
public class Alumno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_alumno")
	private Integer idAlumno;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = false, unique = true)
	private Usuario usuario;

	@Column(nullable = false, unique = true, length = 8)
	private String dni;

	@Column(name = "codigo_estudiante", unique = true, length = 15)
	private String codigoEstudiante;

	@Column(nullable = false, length = 80)
	private String nombres;

	@Column(nullable = false, length = 80)
	private String apellidos;

	@Column(name = "fecha_nacimiento", nullable = false)
	private LocalDate fechaNacimiento;

	@Column(name = "telefono_apoderado", length = 15)
	private String telefonoApoderado;

	@Column(insertable = false)
	private String estado;

	public Alumno() {
	}

	// Getters y Setters
	public Integer getIdAlumno() { return idAlumno; }
	public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

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