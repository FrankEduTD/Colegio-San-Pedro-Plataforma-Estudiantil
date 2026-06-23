package com.sanpedro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aulas")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Integer idAula;

    @Column(nullable = false)
    private String nivel; // 'Inicial', 'Primaria', 'Secundaria'

    @Column(nullable = false, length = 20)
    private String grado; // Ej: '1ro', '2do'

    @Column(nullable = false, length = 10)
    private String seccion; // Ej: 'A', 'B'

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutor_id")
    private Docente tutor;

    @Column(nullable = false, length = 10)
    private String periodo; // Ej: '2025', '2026'

    @Column(columnDefinition = "int default 30")
    private Integer capacidad;

    public Aula() {
    }

    // Getters y Setters
    public Integer getIdAula() { return idAula; }
    public void setIdAula(Integer idAula) { this.idAula = idAula; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }

    public Docente getTutor() { return tutor; }
    public void setTutor(Docente tutor) { this.tutor = tutor; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
}