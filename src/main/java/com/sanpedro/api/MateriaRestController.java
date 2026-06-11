package com.sanpedro.api;

import com.sanpedro.dto.MateriaDto;
import com.sanpedro.model.Materia;
import com.sanpedro.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "*")
public class MateriaRestController {

    private final MateriaService materiaService;

    public MateriaRestController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public List<MateriaDto> listar() {
        return materiaService.listarTodas().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public MateriaDto obtener(@PathVariable Integer id) {
        return toDto(materiaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody MateriaDto dto) {
        try {
            Materia materia = new Materia();
            materia.setNombre(dto.getNombre());
            materia.setArea(dto.getArea());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toDto(materiaService.guardar(materia)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody MateriaDto dto) {
        try {
            Materia materia = new Materia();
            materia.setIdMateria(id);
            materia.setNombre(dto.getNombre());
            materia.setArea(dto.getArea());

            return ResponseEntity.ok(toDto(materiaService.guardar(materia)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            materiaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // METODOS AUXILIARES DE MAPEO
    private MateriaDto toDto(Materia materia) {
        MateriaDto dto = new MateriaDto();
        dto.setIdMateria(materia.getIdMateria());
        dto.setNombre(materia.getNombre());
        dto.setArea(materia.getArea());
        return dto;
    }
}