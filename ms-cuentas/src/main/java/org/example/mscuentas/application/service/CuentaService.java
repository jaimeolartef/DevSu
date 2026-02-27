package org.example.mscuentas.application.service;

import lombok.RequiredArgsConstructor;
import org.example.mscuentas.application.dto.CuentaRequest;
import org.example.mscuentas.application.dto.CuentaResponse;
import org.example.mscuentas.domain.exception.CuentaNotFoundException;
import org.example.mscuentas.domain.model.ClientesInfo;
import org.example.mscuentas.domain.model.Cuenta;
import org.example.mscuentas.domain.repository.ClienteRepository;
import org.example.mscuentas.domain.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

  private final CuentaRepository cuentaRepository;
  private final ClienteRepository clienteRepository;

  @Transactional(readOnly = true)
  public List<CuentaResponse> findAll() {
    return cuentaRepository.findAll().stream().map(this::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public CuentaResponse findByNumeroCuenta(String numeroCuenta) {
    Cuenta cuenta =
        cuentaRepository
            .findByNumeroCuenta(numeroCuenta)
            .orElseThrow(() -> new CuentaNotFoundException(numeroCuenta));
    return toResponse(cuenta);
  }

  @Transactional
  public CuentaResponse create(CuentaRequest request) {
    if (cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta()).isPresent()) {
      throw new IllegalArgumentException(
          "Ya existe una cuenta con número: " + request.getNumeroCuenta());
    }

    ClientesInfo cliente = findCliente(request.getClienteId());

    Cuenta cuenta = new Cuenta();
    cuenta.setClientesInfo(ClientesInfo.builder().id(cliente.getId()).build());
    cuenta.setNumeroCuenta(request.getNumeroCuenta());
    cuenta.setTipoCuenta(request.getTipoCuenta().name());
    cuenta.setSaldoInicial(request.getSaldoInicial());
    cuenta.setSaldoDisponible(request.getSaldoInicial());
    cuenta.setEstado(request.getEstado());

    return toResponse(cuentaRepository.save(cuenta), cliente.getClienteId());
  }

  @Transactional
  public CuentaResponse update(Long id, CuentaRequest request) {
    Cuenta cuenta =
        cuentaRepository
            .findByNumeroCuenta(request.getNumeroCuenta())
            .orElseThrow(() -> new CuentaNotFoundException(request.getNumeroCuenta()));

    ClientesInfo cliente = findCliente(request.getClienteId());

    cuenta.setClientesInfo(ClientesInfo.builder().id(cliente.getId()).build());
    cuenta.setNumeroCuenta(request.getNumeroCuenta());
    cuenta.setTipoCuenta(request.getTipoCuenta().name());
    cuenta.setSaldoInicial(request.getSaldoInicial());
    cuenta.setEstado(request.getEstado());

    return toResponse(cuentaRepository.save(cuenta), cliente.getClienteId());
  }

  private CuentaResponse toResponse(Cuenta cuenta, String clienteId) {
      cuenta.getClientesInfo().setClienteId(clienteId);
      return toResponse(cuenta);
  }

  private CuentaResponse toResponse(Cuenta cuenta) {
    return CuentaResponse.builder()
        .id(cuenta.getId())
        .numeroCuenta(cuenta.getNumeroCuenta())
        .tipoCuenta(cuenta.getTipoCuenta())
        .saldoInicial(cuenta.getSaldoInicial())
        .saldoDisponible(cuenta.getSaldoDisponible())
        .estado(cuenta.isEstado())
        .clienteId(cuenta.getClientesInfo().getClienteId())
        .build();
  }

  private ClientesInfo findCliente(String clienteId) {
    return clienteRepository
        .findByClienteId(clienteId)
        .orElseThrow(() -> new IllegalArgumentException("No existe cliente con ID: " + clienteId));
  }
}
