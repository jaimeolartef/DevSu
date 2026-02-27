package org.example.mscuentas.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.mscuentas.application.enums.ETipoMovimiento;

import java.math.BigDecimal;

@Getter
@Setter
public class MovimientoRequest {

    @NotNull(message = "El valor del movimiento es obligatorio")
    private BigDecimal valor; // positivo = crédito, negativo = débito

    @NotNull(message = "El id de la cuenta es obligatorio")
    private String numeroCuenta;
}
