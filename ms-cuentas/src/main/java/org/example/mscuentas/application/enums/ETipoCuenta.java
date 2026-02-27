package org.example.mscuentas.application.enums;

import lombok.Getter;

@Getter
public enum ETipoCuenta {

	AHORROS("AHORROS"),
	CORRIENTE("CORRIENTE");

	private final String codigo;


	ETipoCuenta(String codigo) {
		this.codigo = codigo;
	}
}
