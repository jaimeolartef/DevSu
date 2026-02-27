package org.example.mscuentas.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.mscuentas.application.dto.ReporteDTO;
import org.example.mscuentas.application.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    /**
     * fechaInicio=2024-01-01
     * fechaFin=2024-12-31
     * cliente=1
     */
    @GetMapping
    public ResponseEntity<ReporteDTO> estadoCuenta(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam String clienteId) {

        return ResponseEntity.ok(reporteService.generarEstadoCuenta(clienteId, fechaInicio, fechaFin));
    }
}
