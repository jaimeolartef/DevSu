package org.example.mscuentas.application.service;


import lombok.RequiredArgsConstructor;
import org.example.mscuentas.application.dto.MovimientoRequest;
import org.example.mscuentas.application.dto.MovimientoResponse;
import org.example.mscuentas.application.enums.ETipoMovimiento;
import org.example.mscuentas.domain.exception.CuentaNotFoundException;
import org.example.mscuentas.domain.exception.MovimientoNotFoundException;
import org.example.mscuentas.domain.exception.SaldoInsuficienteException;
import org.example.mscuentas.domain.model.Cuenta;
import org.example.mscuentas.domain.model.Movimiento;
import org.example.mscuentas.domain.repository.CuentaRepository;
import org.example.mscuentas.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional(readOnly = true)
    public List<MovimientoResponse> findAll() {
        return movimientoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> findByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
            .orElseThrow(() -> new CuentaNotFoundException(numeroCuenta));

        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuenta.getId())
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public MovimientoResponse save(MovimientoRequest request) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .orElseThrow(() -> new CuentaNotFoundException(request.getNumeroCuenta()));

        String tipoMovimiento = request.getValor().compareTo(BigDecimal.ZERO) >= 0
                ? ETipoMovimiento.CREDITO.getCodigo()
                : ETipoMovimiento.DEBITO.getCodigo();
        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(request.getValor());

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException();
        }

        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setValor(request.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCuenta(cuenta);

        return toResponse(movimientoRepository.save(movimiento));
    }

    private MovimientoResponse toResponse(Movimiento movimiento) {
        return MovimientoResponse.builder()
                .id(movimiento.getId())
                .fecha(movimiento.getFecha())
                .tipoMovimiento(ETipoMovimiento.isValidCodigo(ETipoMovimiento.DEBITO, movimiento.getTipoMovimiento()) ? "DEBITO" : "CREDITO")
                .valor(movimiento.getValor())
                .saldo(movimiento.getSaldo())
                .cuentaId(movimiento.getCuenta().getId())
                .build();
    }
}
