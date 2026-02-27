package org.example.mscuentas.domain.repository;

import org.example.mscuentas.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDateTime fechaAfter, LocalDateTime fechaBefore);

	List<Movimiento> findByCuentaId(Long cuentaId);

	List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);
}
