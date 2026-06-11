package com.sanpedro.api;

import com.sanpedro.dto.DocenteDto;
import com.sanpedro.model.Docente;
import com.sanpedro.service.DocenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
public class DocenteRestController {

    private final DocenteService docenteService;

    public DocenteRestController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    public List<DocenteDto> listar(
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado) {

        return docenteService.filtrarDocentes(termino, estado).stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public DocenteDto obtener(@PathVariable Integer id) {
        return toDto(docenteService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody DocenteDto dto) {
        if (dto.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("Debe asignar una cuenta de usuario al docente (usuarioId es obligatorio).");
        }

        try {
            Docente docente = new Docente();
            mapearDatosBasicos(docente, dto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(toDto(docenteService.guardar(docente, dto.getUsuarioId())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody DocenteDto dto) {
        if (dto.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("Debe asignar una cuenta de usuario al docente (usuarioId es obligatorio).");
        }

        try {
            Docente docente = new Docente();
            docente.setIdDocente(id);
            mapearDatosBasicos(docente, dto);

            return ResponseEntity.ok(toDto(docenteService.guardar(docente, dto.getUsuarioId())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        docenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    //mapear
    private void mapearDatosBasicos(Docente docente, DocenteDto dto) {
        docente.setDni(dto.getDni());
        docente.setNombres(dto.getNombres());
        docente.setApellidos(dto.getApellidos());
        docente.setEspecialidad(dto.getEspecialidad());
        docente.setTelefono(dto.getTelefono());
        docente.setEstado(dto.getEstado() == null || dto.getEstado().isBlank() ? "Activo" : dto.getEstado());
    }

    private DocenteDto toDto(Docente docente) {
        DocenteDto dto = new DocenteDto();
        dto.setIdDocente(docente.getIdDocente());
        dto.setUsuarioId(docente.getUsuario().getId());
        dto.setDni(docente.getDni());
        dto.setNombres(docente.getNombres());
        dto.setApellidos(docente.getApellidos());
        dto.setEspecialidad(docente.getEspecialidad());
        dto.setTelefono(docente.getTelefono());
        dto.setEstado(docente.getEstado());
        return dto;
    }
}