package org.example.mscuentas.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.mscuentas.application.enums.ETipoCuenta;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaRequest {

    @NotBlank(message = "El número de cuenta es obligato  erio")
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private ETipoCuenta tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El id del cliente es obligatorio")
    private String clienteId;

}
