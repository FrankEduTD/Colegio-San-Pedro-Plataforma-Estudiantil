package com.sanpedro.api;

import com.sanpedro.dto.AulaDto;
import com.sanpedro.model.Aula;
import com.sanpedro.service.AulaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@CrossOrigin(origins = "*")
public class AulaRestController {

    private final AulaService aulaService;

    public AulaRestController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping
    public List<AulaDto> listar(@RequestParam(required = false) String periodo) {
        List<Aula> aulas = (periodo != null && !periodo.isBlank())
                ? aulaService.listarPorPeriodo(periodo)
                : aulaService.listarTodas();

        return aulas.stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AulaDto obtener(@PathVariable Integer id) {
        return toDto(aulaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody AulaDto dto) {
        try {
            Aula aula = new Aula();
            mapearDatosBasicos(aula, dto);

            Aula guardada = aulaService.guardar(aula, dto.getTutorId());
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(guardada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody AulaDto dto) {
        try {
            Aula aula = new Aula();
            aula.setIdAula(id);
            mapearDatosBasicos(aula, dto);

            Aula actualizada = aulaService.guardar(aula, dto.getTutorId());
            return ResponseEntity.ok(toDto(actualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            aulaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // METODOS AUXILIARES DE MAPEO
    private void mapearDatosBasicos(Aula aula, AulaDto dto) {
        aula.setNivel(dto.getNivel());
        aula.setGrado(dto.getGrado());
        aula.setSeccion(dto.getSeccion());
        aula.setPeriodo(dto.getPeriodo());
        aula.setCapacidad(dto.getCapacidad() == null ? 30 : dto.getCapacidad());
    }

    private AulaDto toDto(Aula aula) {
        AulaDto dto = new AulaDto();
        dto.setIdAula(aula.getIdAula());
        dto.setNivel(aula.getNivel());
        dto.setGrado(aula.getGrado());
        dto.setSeccion(aula.getSeccion());
        dto.setPeriodo(aula.getPeriodo());
        dto.setCapacidad(aula.getCapacidad());
        dto.setTutorId(aula.getTutor() != null ? aula.getTutor().getIdDocente() : null);
        return dto;
    }
}