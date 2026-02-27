package org.example.mscuentas.application.service;

import org.example.mscuentas.application.dto.MovimientoRequest;
import org.example.mscuentas.application.dto.MovimientoResponse;
import org.example.mscuentas.domain.exception.CuentaNotFoundException;
import org.example.mscuentas.domain.exception.SaldoInsuficienteException;
import org.example.mscuentas.domain.model.ClientesInfo;
import org.example.mscuentas.domain.model.Cuenta;
import org.example.mscuentas.domain.model.Movimiento;
import org.example.mscuentas.domain.repository.CuentaRepository;
import org.example.mscuentas.domain.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovimientoService - Pruebas Unitarias")
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuentaConSaldo;

    @BeforeEach
    void setUp() {
        cuentaConSaldo = new Cuenta();
        cuentaConSaldo.setId(1L);
        cuentaConSaldo.setNumeroCuenta("478758");
        cuentaConSaldo.setTipoCuenta("AHORROS");
        cuentaConSaldo.setSaldoInicial(new BigDecimal("2000.00"));
        cuentaConSaldo.setSaldoDisponible(new BigDecimal("2000.00"));
        cuentaConSaldo.setEstado(true);
        cuentaConSaldo.setClientesInfo(ClientesInfo.builder().id(1L).build());
    }

    @Test
    @DisplayName("Debe registrar un movimiento crédito y actualizar el saldo")
    void registrar_credito_debeAumentarSaldo() {
        MovimientoRequest request = new MovimientoRequest();
        request.setNumeroCuenta("1234567814");
        request.setValor(new BigDecimal("-500.00"));

        Movimiento movimientoGuardado = new Movimiento();
        movimientoGuardado.setId(1L);
        movimientoGuardado.setFecha(LocalDateTime.now());
        movimientoGuardado.setValor(request.getValor());
        movimientoGuardado.setSaldo(new BigDecimal("1500.00"));
        movimientoGuardado.setTipoMovimiento("CREDITO");
        movimientoGuardado.setCuenta(cuentaConSaldo);

        when(cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())).thenReturn(Optional.of(cuentaConSaldo));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoGuardado);

        MovimientoResponse response = movimientoService.save(request);

        assertThat(response.getTipoMovimiento()).isEqualTo("CREDITO");
        assertThat(response.getSaldo()).isEqualByComparingTo("1500.00");
        assertThat(cuentaConSaldo.getSaldoDisponible()).isEqualByComparingTo("1500.00");
        verify(cuentaRepository).save(cuentaConSaldo);
    }

    @Test
    @DisplayName("Debe registrar un débito y reducir el saldo")
    void registrar_debito_debeReducirSaldo() {
        MovimientoRequest request = new MovimientoRequest();
        request.setNumeroCuenta("1234567814");
        request.setValor(new BigDecimal("500.00"));

        Movimiento movimientoGuardado = new Movimiento();
        movimientoGuardado.setId(2L);
        movimientoGuardado.setFecha(LocalDateTime.now());
        movimientoGuardado.setValor(request.getValor());
        movimientoGuardado.setSaldo(new BigDecimal("2500.00"));
        movimientoGuardado.setTipoMovimiento("DEBITO");
        movimientoGuardado.setCuenta(cuentaConSaldo);

        when(cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())).thenReturn(Optional.of(cuentaConSaldo));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoGuardado);

        MovimientoResponse response = movimientoService.save(request);

        assertThat(response.getTipoMovimiento()).isEqualTo("DEBITO");
        assertThat(response.getSaldo()).isEqualByComparingTo("2500.00");
    }

    @Test
    @DisplayName("Debe lanzar SaldoInsuficienteException cuando el saldo no alcanza")
    void registrar_saldoInsuficiente_debeLanzarExcepcion() {
        MovimientoRequest request = new MovimientoRequest();
        request.setNumeroCuenta("1234567814");
        request.setValor(new BigDecimal("-10000.00")); // mayor que el saldo disponible

        when(cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())).thenReturn(Optional.of(cuentaConSaldo));

        assertThatThrownBy(() -> movimientoService.save(request))
                .isInstanceOf(SaldoInsuficienteException.class)
                .hasMessage("Saldo no disponible");

        verify(movimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar CuentaNotFoundException cuando la cuenta no existe")
    void registrar_cuentaNoExiste_debeLanzarExcepcion() {
        MovimientoRequest request = new MovimientoRequest();
        request.setNumeroCuenta("12345678444");
        request.setValor(new BigDecimal("100.00"));

        when(cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movimientoService.save(request))
                .isInstanceOf(CuentaNotFoundException.class);
    }
}
