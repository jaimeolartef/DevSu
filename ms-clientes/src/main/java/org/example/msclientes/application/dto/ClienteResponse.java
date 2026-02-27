package org.example.msclientes.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClienteResponse {

    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String tipoDocumento;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String clienteId;
    private Boolean estado;

}
