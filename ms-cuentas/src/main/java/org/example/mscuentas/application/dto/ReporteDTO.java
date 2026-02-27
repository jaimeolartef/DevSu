package org.example.mscuentas.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ReporteDTO {

    private String clienteId;
    private String clienteNombre;

    private List<CuentaReporteDTO> cuentas;

    @Getter
    @Builder
    public static class CuentaReporteDTO {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoInicial;
        private BigDecimal saldoDisponible;
        private Boolean estado;
        private List<MovimientoResponse> movimientos;
    }
}
