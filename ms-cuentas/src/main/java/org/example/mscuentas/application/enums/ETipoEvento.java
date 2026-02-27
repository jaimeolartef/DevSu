package org.example.mscuentas.application.enums;

import lombok.Getter;

@Getter
public enum ETipoEvento {

	CREATED("CREATED"),
	UPDATED("UPDATED"),
	DELETED("DELETED");

	private final String codigo;

	ETipoEvento(String codigo) {
		this.codigo = codigo;
	}

	public static ETipoEvento fromCodigo(String codigo) {
		for (ETipoEvento tipoEvento : ETipoEvento.values()) {
			if (tipoEvento.getCodigo().equals(codigo)) {
				return tipoEvento;
			}
		}
		return null;
	}

	public static boolean isValidCodigo(ETipoEvento tipoEvento, String codigo) {
		return tipoEvento.getCodigo().equals(codigo);
	}
}
