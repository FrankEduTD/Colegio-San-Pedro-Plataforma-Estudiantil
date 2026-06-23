package com.sanpedro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones_bimestrales")
public class CalificacionBimestral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private Integer idCalificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_matricula_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DetalleMatricula detalleMatricula;

    @Column(name = "numero_bimestre", nullable = false)
    private Integer numeroBimestre;

    @Column(name = "ev_1", precision = 4, scale = 2)
    private BigDecimal ev1;

    @Column(name = "ev_2", precision = 4, scale = 2)
    private BigDecimal ev2;

    @Column(name = "ev_3", precision = 4, scale = 2)
    private BigDecimal ev3;

    @Column(name = "ev_final", precision = 4, scale = 2)
    private BigDecimal evFinal;

    @Column(name = "promedio_bimestre", precision = 4, scale = 2)
    private BigDecimal promedioBimestre;

    @Column(name = "letra_bimestre", length = 2)
    private String letraBimestre;

    @Column(name = "fecha_actualizacion", insertable = false, updatable = false)
    private LocalDateTime fechaActualizacion;

    // Getters y Setters...
    public Integer getIdCalificacion() { return idCalificacion; }
    public void setIdCalificacion(Integer idCalificacion) { this.idCalificacion = idCalificacion; }
    public DetalleMatricula getDetalleMatricula() { return detalleMatricula; }
    public void setDetalleMatricula(DetalleMatricula detalleMatricula) { this.detalleMatricula = detalleMatricula; }
    public Integer getNumeroBimestre() { return numeroBimestre; }
    public void setNumeroBimestre(Integer numeroBimestre) { this.numeroBimestre = numeroBimestre; }
    public BigDecimal getEv1() { return ev1; }
    public void setEv1(BigDecimal ev1) { this.ev1 = ev1; }
    public BigDecimal getEv2() { return ev2; }
    public void setEv2(BigDecimal ev2) { this.ev2 = ev2; }
    public BigDecimal getEv3() { return ev3; }
    public void setEv3(BigDecimal ev3) { this.ev3 = ev3; }
    public BigDecimal getEvFinal() { return evFinal; }
    public void setEvFinal(BigDecimal evFinal) { this.evFinal = evFinal; }
    public BigDecimal getPromedioBimestre() { return promedioBimestre; }
    public void setPromedioBimestre(BigDecimal promedioBimestre) { this.promedioBimestre = promedioBimestre; }
    public String getLetraBimestre() { return letraBimestre; }
    public void setLetraBimestre(String letraBimestre) { this.letraBimestre = letraBimestre; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}