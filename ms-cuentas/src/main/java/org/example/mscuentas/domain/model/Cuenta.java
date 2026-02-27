package org.example.mscuentas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter@Entity
@Table(name = "cuentas")
public class Cuenta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Size(max = 20)
	@NotNull
	@Column(name = "numero_cuenta", nullable = false, length = 20)
	private String numeroCuenta;

	@Size(max = 20)
	@NotNull
	@Column(name = "tipo_cuenta", nullable = false, length = 20)
	private String tipoCuenta;

	@NotNull
	@ColumnDefault("0.00")
	@Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
	private BigDecimal saldoInicial;

	@NotNull
	@ColumnDefault("0.00")
	@Column(name = "saldo_disponible", nullable = false, precision = 15, scale = 2)
	private BigDecimal saldoDisponible;

	@NotNull
	@Column(name = "estado", nullable = false)
	private boolean estado;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false)
	private ClientesInfo clientesInfo;
}