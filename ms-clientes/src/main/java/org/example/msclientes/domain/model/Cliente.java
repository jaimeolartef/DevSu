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
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Size(max = 100)
	@NotNull
	@Column(name = "nombre", nullable = false, length = 100)
	private String nombre;

	@Size(max = 2)
	@NotNull
	@Column(name = "tipo_documento", nullable = false, length = 2)
	private String tipoDocumento;

	@Size(max = 1)
	@Column(name = "genero", nullable = false)
	private String genero;

	@Column(name = "edad", nullable = false)
	private Integer edad;

	@Size(max = 20)
	@NotNull
	@Column(name = "identificacion", nullable = false, length = 20)
	private String identificacion;

	@Size(max = 200)
	@Column(name = "direccion", length = 200)
	private String direccion;

	@Size(max = 20)
	@Column(name = "telefono", length = 20)
	private String telefono;

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