package org.example.mscuentas.application.service;


import lombok.RequiredArgsConstructor;
import org.example.mscuentas.application.dto.MovimientoResponse;
import org.example.mscuentas.application.dto.ReporteDTO;
import org.example.mscuentas.domain.exception.RecursoNoEncontradoException;
import org.example.mscuentas.domain.model.ClientesInfo;
import org.example.mscuentas.domain.model.Cuenta;
import org.example.mscuentas.domain.model.Movimiento;
import org.example.mscuentas.domain.repository.ClienteRepository;
import org.example.mscuentas.domain.repository.CuentaRepository;
import org.example.mscuentas.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ClienteRepository clienteInfoRepository;

    @Transactional(readOnly = true)
    public ReporteDTO generarEstadoCuenta(String clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        ClientesInfo clienteInfo = clienteInfoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + clienteId));

        LocalDateTime desde = fechaInicio.atStartOfDay();
        LocalDateTime hasta = fechaFin.atTime(LocalTime.MAX);

        List<Cuenta> cuentas = cuentaRepository.findByClientesInfoId(clienteInfo.getId());

    List<ReporteDTO.CuentaReporteDTO> cuentasReporte =
        cuentas.stream()
            .map(
                cuenta -> {
                  List<Movimiento> movimientos =
                      movimientoRepository.findByCuentaIdAndFechaBetween(
                          cuenta.getId(), desde, hasta);

                  List<MovimientoResponse> movimientosDTO =
                      movimientos.stream()
                          .map(
                              m ->
                                  MovimientoResponse.builder()
                                      .id(m.getId())
                                      .fecha(m.getFecha())
                                      .tipoMovimiento(m.getTipoMovimiento())
                                      .valor(m.getValor())
                                      .saldo(m.getSaldo())
                                      .cuentaId(cuenta.getId())
                                      .build())
                          .toList();

                  return ReporteDTO.CuentaReporteDTO.builder()
                      .numeroCuenta(cuenta.getNumeroCuenta())
                      .tipoCuenta(cuenta.getTipoCuenta())
                      .saldoInicial(cuenta.getSaldoInicial())
                      .saldoDisponible(cuenta.getSaldoDisponible())
                      .estado(cuenta.isEstado())
                      .movimientos(movimientosDTO)
                      .build();
                })
            .toList();

        return ReporteDTO.builder()
                .clienteId(clienteInfo.getClienteId())
                .clienteNombre(clienteInfo.getNombre())
                .cuentas(cuentasReporte)
                .build();
    }
}
