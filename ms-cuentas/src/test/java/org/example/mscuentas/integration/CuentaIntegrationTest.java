package org.example.mscuentas.integration;


import org.example.mscuentas.application.dto.CuentaRequest;
import org.example.mscuentas.application.dto.MovimientoRequest;
import org.example.mscuentas.application.enums.ETipoCuenta;
import org.example.mscuentas.domain.model.ClientesInfo;
import org.example.mscuentas.domain.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integración - Cuentas y Movimientos")
class CuentaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("Flujo completo: crear cuenta y registrar movimiento")
  void flujoCompleto_crearCuentaYMovimiento() throws Exception {
    // 1.Crear cliente

    clienteRepository.save(
        ClientesInfo.builder()
            .clienteId("CLI003")
            .nombre("Cliente Test")
            .identificacion("12345678")
            .estado(true)
            .build());

    // 2. Crear cuenta
    CuentaRequest cuentaRequest = new CuentaRequest();
    cuentaRequest.setNumeroCuenta("TEST-001");
    cuentaRequest.setTipoCuenta(ETipoCuenta.AHORROS);
    cuentaRequest.setSaldoInicial(new BigDecimal("1000.00"));
    cuentaRequest.setEstado(true);
    cuentaRequest.setClienteId("CLI003");

    mockMvc
        .perform(
            post("/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.numeroCuenta").value("TEST-001"))
        .andExpect(jsonPath("$.saldoDisponible").value(1000.00));

    // 3. Registrar movimiento (débito)
    MovimientoRequest movRequest = new MovimientoRequest();
    movRequest.setNumeroCuenta(cuentaRequest.getNumeroCuenta());
    movRequest.setValor(new BigDecimal("-200.00"));

    mockMvc
        .perform(
            post("/movimientos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.tipoMovimiento").value("DEBITO"))
        .andExpect(jsonPath("$.saldo").value(800.00));
  }
}
