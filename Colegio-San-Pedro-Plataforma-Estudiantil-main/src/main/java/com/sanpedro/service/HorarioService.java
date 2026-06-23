package com.sanpedro.service;

import com.sanpedro.dto.HorarioDto;
import com.sanpedro.model.CursoProgramado;
import com.sanpedro.model.Horario;
import com.sanpedro.repository.CursoProgramadoRepository;
import com.sanpedro.repository.HorarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final CursoProgramadoRepository cursoProgramadoRepository;

    public HorarioService(HorarioRepository horarioRepository,
                         CursoProgramadoRepository cursoProgramadoRepository) {
        this.horarioRepository = horarioRepository;
        this.cursoProgramadoRepository = cursoProgramadoRepository;
    }

    public List<Horario> listarTodos() {
        return horarioRepository.findAll();
    }

    public List<Horario> listarPorAula(Integer aulaId) {
        return horarioRepository.findByCursoProgramado_Aula_IdAula(aulaId);
    }

    public Horario obtenerPorId(Integer id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
    }

    public Horario guardar(HorarioDto dto) {
        CursoProgramado cursoProgramado = cursoProgramadoRepository.findById(dto.getCursoProgramadoId())
                .orElseThrow(() -> new RuntimeException("Curso programado no existe."));

        Horario horario;
        if (dto.getIdHorario() == null) {
            horario = new Horario();
            horario.setEstado("Activo");
        } else {
            horario = obtenerPorId(dto.getIdHorario());
            horario.setEstado(dto.getEstado() != null ? dto.getEstado() : horario.getEstado());
        }

        horario.setCursoProgramado(cursoProgramado);
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());

        return horarioRepository.save(horario);
    }

    public void eliminar(Integer id) {
        Horario horario = obtenerPorId(id);
        horario.setEstado("Inactivo");
        horarioRepository.save(horario);
    }
}
