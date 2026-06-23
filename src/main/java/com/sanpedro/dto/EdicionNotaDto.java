package com.sanpedro.dto;

import java.math.BigDecimal;

public class EdicionNotaDto {
    private BigDecimal ev1;
    private BigDecimal ev2;
    private BigDecimal ev3;
    private BigDecimal evFinal;

    // Getters y Setters
    public BigDecimal getEv1() { return ev1; }
    public void setEv1(BigDecimal ev1) { this.ev1 = ev1; }
    public BigDecimal getEv2() { return ev2; }
    public void setEv2(BigDecimal ev2) { this.ev2 = ev2; }
    public BigDecimal getEv3() { return ev3; }
    public void setEv3(BigDecimal ev3) { this.ev3 = ev3; }
    public BigDecimal getEvFinal() { return evFinal; }
    public void setEvFinal(BigDecimal evFinal) { this.evFinal = evFinal; }
}