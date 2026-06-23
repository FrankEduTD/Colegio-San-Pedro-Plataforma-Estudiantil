package com.sanpedro.dto;

import java.math.BigDecimal;

public class CalificacionResponseDto {
    private Integer idCalificacion;
    private Integer numeroBimestre;
    private BigDecimal ev1;
    private BigDecimal ev2;
    private BigDecimal ev3;
    private BigDecimal evFinal;
    private BigDecimal promedioBimestre;
    private String letraBimestre;

    // Getters y Setters
    public Integer getIdCalificacion() { return idCalificacion; }
    public void setIdCalificacion(Integer idCalificacion) { this.idCalificacion = idCalificacion; }
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
}