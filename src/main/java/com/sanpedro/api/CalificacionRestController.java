package com.sanpedro.api;

import com.sanpedro.dto.AlumnoCalificacionDto; // <-- AQUÍ ESTÁ LA SOLUCIÓN
import com.sanpedro.dto.CalificacionResponseDto;
import com.sanpedro.dto.EdicionNotaDto;
import com.sanpedro.model.CalificacionBimestral;
import com.sanpedro.service.CalificacionAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@CrossOrigin(origins = "*") // Importante para Angular
public class CalificacionRestController {

    @Autowired
    private CalificacionAdminService calificacionAdminService;

    // GET: Listar la planilla de notas de un curso y bimestre
    @GetMapping("/curso/{cursoProgId}/bimestre/{numeroBimestre}")
    public ResponseEntity<?> listarNotasPorCursoYBimestre(
            @PathVariable Integer cursoProgId,
            @PathVariable Integer numeroBimestre) {

        try {
            List<AlumnoCalificacionDto> planilla = calificacionAdminService.obtenerPlanillaNotas(cursoProgId, numeroBimestre);
            return new ResponseEntity<>(planilla, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al obtener las notas: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT: Forzar edición de notas de un alumno en un curso específico
    @PutMapping("/detalle/{idDetalle}/bimestre/{numeroBimestre}")
    public ResponseEntity<?> forzarEdicionAdmin(
            @PathVariable Integer idDetalle,
            @PathVariable Integer numeroBimestre,
            @RequestBody EdicionNotaDto notasNuevas) {

        try {
            CalificacionBimestral calificacionDB = calificacionAdminService
                    .forzarEdicionNotas(idDetalle, numeroBimestre, notasNuevas);
            CalificacionResponseDto response = new CalificacionResponseDto();
            response.setIdCalificacion(calificacionDB.getIdCalificacion());
            response.setNumeroBimestre(calificacionDB.getNumeroBimestre());
            response.setEv1(calificacionDB.getEv1());
            response.setEv2(calificacionDB.getEv2());
            response.setEv3(calificacionDB.getEv3());
            response.setEvFinal(calificacionDB.getEvFinal());
            response.setPromedioBimestre(calificacionDB.getPromedioBimestre());
            response.setLetraBimestre(calificacionDB.getLetraBimestre());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al actualizar las calificaciones: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}