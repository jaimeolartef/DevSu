package org.example.mscuentas.domain.repository;

import org.example.mscuentas.domain.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

	Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

	List<Cuenta> findByClientesInfoId(Long clienteId);
}