package com.sanpedro.api;

import com.sanpedro.dto.PagoPensionResponseDto;
import com.sanpedro.model.PagoPension;
import com.sanpedro.service.TesoreriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tesoreria")
public class TesoreriaRestController {

    @Autowired
    private TesoreriaService tesoreriaService;

    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<?> listarPagosDeAlumno(@PathVariable Integer idMatricula) {
        try {
            List<PagoPension> cronograma = tesoreriaService.obtenerPagosPorMatricula(idMatricula);

            if(cronograma.isEmpty()) {
                return new ResponseEntity<>("No se encontraron pagos para esta matrícula.", HttpStatus.NOT_FOUND);
            }

            List<PagoPensionResponseDto> responseList = cronograma.stream().map(this::convertirADto).collect(Collectors.toList());

            return new ResponseEntity<>(responseList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los pagos: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/pagar/{idPago}")
    public ResponseEntity<?> registrarPago(@PathVariable Integer idPago, @RequestBody Map<String, String> requestData) {
        try {
            String metodoPago = requestData.getOrDefault("metodoPago", "Efectivo");
            PagoPension reciboPagado = tesoreriaService.procesarPago(idPago, metodoPago);
            PagoPensionResponseDto response = convertirADto(reciboPagado);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno en el servidor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PagoPensionResponseDto convertirADto(PagoPension pago) {
        PagoPensionResponseDto dto = new PagoPensionResponseDto();
        dto.setIdPago(pago.getIdPago());
        dto.setConcepto(pago.getConcepto());
        dto.setMonto(pago.getMonto());
        dto.setFechaVencimiento(pago.getFechaVencimiento());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstado(pago.getEstado());
        dto.setMetodoPago(pago.getMetodoPago());
        return dto;
    }
}