package com.sanpedro.api;

import com.sanpedro.dto.MatriculaRequestDto;
import com.sanpedro.dto.MatriculaResponseDto;
import com.sanpedro.model.Matricula;
import com.sanpedro.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matriculas")
@CrossOrigin(origins = "*") // Importante para que Angular pueda conectarse
public class MatriculaRestController {

    @Autowired
    private MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDto>> listarMatriculas() {
        List<Matricula> matriculas = matriculaService.listarTodas();

        List<MatriculaResponseDto> dtos = matriculas.stream().map(m -> {
            MatriculaResponseDto dto = new MatriculaResponseDto();
            dto.setIdMatricula(m.getIdMatricula());
            dto.setAlumnoNombres(m.getAlumno().getNombres());
            dto.setAlumnoApellidos(m.getAlumno().getApellidos());
            dto.setAulaNivel(m.getAula().getNivel());
            dto.setAulaGrado(m.getAula().getGrado());
            dto.setAulaSeccion(m.getAula().getSeccion());
            dto.setFechaMatricula(m.getFechaMatricula());
            dto.setEstado(m.getEstado());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarMatricula(@RequestBody MatriculaRequestDto request) {
        try {
            Matricula matriculaDB = matriculaService.registrarMatricula(request);
            MatriculaResponseDto response = new MatriculaResponseDto();
            response.setIdMatricula(matriculaDB.getIdMatricula());
            response.setAlumnoNombres(matriculaDB.getAlumno().getNombres());
            response.setAlumnoApellidos(matriculaDB.getAlumno().getApellidos());
            response.setAulaNivel(matriculaDB.getAula().getNivel());
            response.setAulaGrado(matriculaDB.getAula().getGrado());
            response.setAulaSeccion(matriculaDB.getAula().getSeccion());
            response.setFechaMatricula(matriculaDB.getFechaMatricula());
            response.setEstado(matriculaDB.getEstado());

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno en el servidor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}