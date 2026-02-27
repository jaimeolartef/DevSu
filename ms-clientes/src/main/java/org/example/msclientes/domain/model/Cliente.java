package org.example.msclientes.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter@Entity
@Table(name = "clientes")
public class Cliente extends Persona {


	@Size(max = 50)
	@NotNull
	@Column(name = "cliente_id", nullable = false, length = 50)
	private String clienteId;

	@Size(max = 255)
	@NotNull
	@Column(name = "contrasena", nullable = false)
	private String contrasena;

	@NotNull
	@ColumnDefault("true")
	@Column(name = "estado", nullable = false)
	private Boolean estado;



}