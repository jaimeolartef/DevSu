package org.example.msclientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEvent implements Serializable {

    private String eventType;
    private Long id;
    private String clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
}
