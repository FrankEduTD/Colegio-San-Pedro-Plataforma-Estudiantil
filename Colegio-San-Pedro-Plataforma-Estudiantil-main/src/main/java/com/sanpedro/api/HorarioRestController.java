package com.sanpedro.api;

import com.sanpedro.dto.HorarioDto;
import com.sanpedro.model.Horario;
import com.sanpedro.service.HorarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioRestController {

    private final HorarioService service;

    public HorarioRestController(HorarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<HorarioDto> listar(@RequestParam(required = false) Integer aulaId) {
        return (aulaId != null ? service.listarPorAula(aulaId) : service.listarTodos())
                .stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public HorarioDto obtener(@PathVariable Integer id) {
        return toDto(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody HorarioDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(service.guardar(dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody HorarioDto dto) {
        try {
            dto.setIdHorario(id);
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

    private HorarioDto toDto(Horario horario) {
        HorarioDto dto = new HorarioDto();
        dto.setIdHorario(horario.getIdHorario());
        dto.setCursoProgramadoId(horario.getCursoProgramado().getIdCursoProg());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setEstado(horario.getEstado());

        dto.setCursoNombre(horario.getCursoProgramado().getMateria().getNombre());
        dto.setDocenteNombre(horario.getCursoProgramado().getDocente().getNombres() + " " + horario.getCursoProgramado().getDocente().getApellidos());
        dto.setGrado(horario.getCursoProgramado().getAula().getGrado());
        dto.setSeccion(horario.getCursoProgramado().getAula().getSeccion());
        dto.setPeriodo(horario.getCursoProgramado().getAula().getPeriodo());
        return dto;
    }
}
