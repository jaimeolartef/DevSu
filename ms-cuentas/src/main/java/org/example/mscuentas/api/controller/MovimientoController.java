package org.example.mscuentas.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mscuentas.application.dto.MovimientoRequest;
import org.example.mscuentas.application.dto.MovimientoResponse;
import org.example.mscuentas.application.service.MovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> findAll() {
        return ResponseEntity.ok(movimientoService.findAll());
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<MovimientoResponse>> findById(@PathVariable String numeroCuenta) {
        return ResponseEntity.ok(movimientoService.findByNumeroCuenta(numeroCuenta));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> save(@Valid @RequestBody MovimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.save(request));
    }
}
