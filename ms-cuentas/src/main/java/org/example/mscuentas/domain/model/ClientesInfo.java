package org.example.mscuentas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@Getter
@Setter@Entity
@Table(name = "clientes_info")
@AllArgsConstructor
public class ClientesInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Size(max = 50)
	@NotNull
	@Column(name = "cliente_id", nullable = false, length = 50)
	private String clienteId;

	@Size(max = 100)
	@NotNull
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Size(max = 20)
	@NotNull
	@Column(name = "identificacion", nullable = false, length = 20)
	private String identificacion;

	@NotNull
	@Column(name = "estado", nullable = false)
	private boolean estado;

}