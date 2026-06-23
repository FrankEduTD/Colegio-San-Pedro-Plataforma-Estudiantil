package com.sanpedro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos_programados")
public class CursoProgramado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso_prog")
    private Integer idCursoProg;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;

    @Column(insertable = false)
    private String estado;

    public CursoProgramado() {
    }

    // Getters y Setters
    public Integer getIdCursoProg() { return idCursoProg; }
    public void setIdCursoProg(Integer idCursoProg) { this.idCursoProg = idCursoProg; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }

    public Aula getAula() { return aula; }
    public void setAula(Aula aula) { this.aula = aula; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}