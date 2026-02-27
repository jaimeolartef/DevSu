package org.example.mscuentas.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ClienteEvent implements Serializable {
    private String eventType;
    private Long id;
    private String clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
}
