package com.sanpedro.api;

import com.sanpedro.dto.CursoProgramadoDto;
import com.sanpedro.model.CursoProgramado;
import com.sanpedro.service.CursoProgramadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos-programados")
@CrossOrigin(origins = "*")
public class CursoProgramadoRestController {

    private final CursoProgramadoService service;

    public CursoProgramadoRestController(CursoProgramadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CursoProgramadoDto> listar(@RequestParam(required = false) Integer aulaId) {
        List<CursoProgramado> lista = (aulaId != null)
                ? service.listarPorAula(aulaId)
                : service.listarTodos();

        return lista.stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public CursoProgramadoDto obtener(@PathVariable Integer id) {
        return toDto(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CursoProgramadoDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(service.guardar(dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody CursoProgramadoDto dto) {
        try {
            dto.setIdCursoProg(id);
            return ResponseEntity.ok(toDto(service.guardar(dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // METODOS AUXILIARES DE MAPEO
    private CursoProgramadoDto toDto(CursoProgramado cp) {
        CursoProgramadoDto dto = new CursoProgramadoDto();
        dto.setIdCursoProg(cp.getIdCursoProg());
        dto.setMateriaId(cp.getMateria().getIdMateria());
        dto.setDocenteId(cp.getDocente().getIdDocente());
        dto.setAulaId(cp.getAula().getIdAula());
        dto.setEstado(cp.getEstado());
        return dto;
    }
}