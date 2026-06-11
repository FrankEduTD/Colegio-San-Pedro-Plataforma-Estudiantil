package com.sanpedro.service;

import com.sanpedro.model.PagoPension;
import com.sanpedro.repository.PagoPensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TesoreriaService {

    @Autowired
    private PagoPensionRepository pagoPensionRepository;

    public List<PagoPension> obtenerPagosPorMatricula(Integer idMatricula) {
        return pagoPensionRepository.findByMatricula_IdMatricula(idMatricula);
    }

    @Transactional
    public PagoPension procesarPago(Integer idPago, String metodoPago) {
        // 1. Buscamos el recibo en la BD
        PagoPension pago = pagoPensionRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Recibo de pago no encontrado con el ID: " + idPago));

        // 2. Validamos que no esté pagado ya
        if ("Pagado".equals(pago.getEstado())) {
            throw new RuntimeException("Operación rechazada: Este recibo ya se encuentra pagado.");
        }

        // 3. Actualizamos los datos
        pago.setEstado("Pagado");
        pago.setFechaPago(LocalDate.now()); // Registra la fecha de hoy automáticamente
        pago.setMetodoPago(metodoPago);

        // 4. Guardamos los cambios
        return pagoPensionRepository.save(pago);
    }
}