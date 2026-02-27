package org.example.msclientes.application.enums;

import lombok.Getter;

@Getter
public enum ETipoDocumento {
 CC("CC"),
 PASAPORTE("PS"),
 CEDULA_EXTRANJERIA("CE"),
 TARJETA_IDENTIDAD("TI"),
 OTRO("OTR");

 private final String codigo;

 ETipoDocumento(String codigo) {
 this.codigo = codigo;
 }

}
