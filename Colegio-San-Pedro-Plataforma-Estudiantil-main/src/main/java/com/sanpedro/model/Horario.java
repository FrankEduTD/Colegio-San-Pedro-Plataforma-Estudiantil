package com.sanpedro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_prog_id", nullable = false)
    private CursoProgramado cursoProgramado;

    @Column(name = "dia_semana", nullable = false, length = 20)
    private String diaSemana;

    @Column(name = "hora_inicio", nullable = false, length = 10)
    private String horaInicio;

    @Column(name = "hora_fin", nullable = false, length = 10)
    private String horaFin;

    @Column(insertable = false)
    private String estado;

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public CursoProgramado getCursoProgramado() {
        return cursoProgramado;
    }

    public void setCursoProgramado(CursoProgramado cursoProgramado) {
        this.cursoProgramado = cursoProgramado;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
