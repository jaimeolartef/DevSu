package org.example.msclientes.domain.repository;

import org.example.msclientes.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Optional<Cliente> findByIdentificacion(String identificacion);

	Optional<Cliente> findByClienteId(String clienteId);

}