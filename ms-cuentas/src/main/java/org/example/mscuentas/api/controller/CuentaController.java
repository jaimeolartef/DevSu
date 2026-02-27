package org.example.mscuentas.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.mscuentas.application.dto.CuentaRequest;
import org.example.mscuentas.application.dto.CuentaResponse;
import org.example.mscuentas.application.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> findAll() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaResponse> findByNumeroCuenta(@PathVariable String numeroCuenta) {
        return ResponseEntity.ok(cuentaService.findByNumeroCuenta(numeroCuenta));
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> create(@Valid @RequestBody CuentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody CuentaRequest request) {
        return ResponseEntity.ok(cuentaService.update(id, request));
    }
}
