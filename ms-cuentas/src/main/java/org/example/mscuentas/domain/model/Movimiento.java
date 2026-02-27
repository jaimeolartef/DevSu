package org.example.mscuentas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter@Entity
@Table(name = "movimientos")
public class Movimiento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull
	@Column(name = "fecha", nullable = false)
	private LocalDateTime fecha;

	@Size(max = 2)
	@Column(name = "tipo_movimiento", length = 2)
	private String tipoMovimiento;

	@NotNull
	@Column(name = "valor", nullable = false, precision = 15, scale = 2)
	private BigDecimal valor;

	@NotNull
	@Column(name = "saldo", nullable = false, precision = 15, scale = 2)
	private BigDecimal saldo;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "cuenta_id", nullable = false)
	private Cuenta cuenta;
}