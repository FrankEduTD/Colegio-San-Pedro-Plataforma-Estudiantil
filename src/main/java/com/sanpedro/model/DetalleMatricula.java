package com.sanpedro.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "detalle_matriculas")
public class DetalleMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_prog_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CursoProgramado cursoProgramado;

    @Column(name = "promedio_final")
    private BigDecimal promedioFinal;

    @Column(name = "letra_final", length = 2)
    private String letraFinal;

    @Column(name = "estado_curso", insertable = false)
    private String estadoCurso; // 'Aprobado', 'Desaprobado', 'En curso'

    // Getters y Setters
    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }
    public Matricula getMatricula() { return matricula; }
    public void setMatricula(Matricula matricula) { this.matricula = matricula; }
    public CursoProgramado getCursoProgramado() { return cursoProgramado; }
    public void setCursoProgramado(CursoProgramado cursoProgramado) { this.cursoProgramado = cursoProgramado; }
    public BigDecimal getPromedioFinal() { return promedioFinal; }
    public void setPromedioFinal(BigDecimal promedioFinal) { this.promedioFinal = promedioFinal; }
    public String getLetraFinal() { return letraFinal; }
    public void setLetraFinal(String letraFinal) { this.letraFinal = letraFinal; }
    public String getEstadoCurso() { return estadoCurso; }
    public void setEstadoCurso(String estadoCurso) { this.estadoCurso = estadoCurso; }
}