package com.sanpedro.dto;

import java.math.BigDecimal;
import java.util.List;

public class AlumnoCursoNotasDto {

    private Integer idDetalle;
    private Integer idCursoProg;
    private String materia;
    private String docente;
    private String grado;
    private String seccion;
    private String periodo;
    private String estadoCurso;
    private BigDecimal promedioFinal;
    private String letraFinal;
    private List<CalificacionResponseDto> calificacionesBimestrales;

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }
    public Integer getIdCursoProg() { return idCursoProg; }
    public void setIdCursoProg(Integer idCursoProg) { this.idCursoProg = idCursoProg; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public String getDocente() { return docente; }
    public void setDocente(String docente) { this.docente = docente; }
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getEstadoCurso() { return estadoCurso; }
    public void setEstadoCurso(String estadoCurso) { this.estadoCurso = estadoCurso; }
    public BigDecimal getPromedioFinal() { return promedioFinal; }
    public void setPromedioFinal(BigDecimal promedioFinal) { this.promedioFinal = promedioFinal; }
    public String getLetraFinal() { return letraFinal; }
    public void setLetraFinal(String letraFinal) { this.letraFinal = letraFinal; }
    public List<CalificacionResponseDto> getCalificacionesBimestrales() { return calificacionesBimestrales; }
    public void setCalificacionesBimestrales(List<CalificacionResponseDto> calificacionesBimestrales) { this.calificacionesBimestrales = calificacionesBimestrales; }
}
