package org.example.mscuentas.domain.repository;

import org.example.mscuentas.domain.model.ClientesInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClientesInfo, Long> {
	Optional<ClientesInfo> findByClienteId(String clienteId);
}