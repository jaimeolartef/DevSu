package org.example.mscuentas.application.enums;

import lombok.Getter;

@Getter
public enum ETipoMovimiento {
  DEBITO("DB"),
  CREDITO("CR");

  private final String codigo;

  ETipoMovimiento(String codigo) {
   this.codigo = codigo;
  }

  public static ETipoMovimiento fromCodigo(String codigo) {
    for (ETipoMovimiento tipoMovimiento : ETipoMovimiento.values()) {
      if (tipoMovimiento.getCodigo().equals(codigo)) {
        return tipoMovimiento;
      }
    }
    return null;
  }

  public static boolean isValidCodigo(ETipoMovimiento tipoMovimiento, String codigo) {
    return tipoMovimiento.getCodigo().equals(codigo);
  }
}
