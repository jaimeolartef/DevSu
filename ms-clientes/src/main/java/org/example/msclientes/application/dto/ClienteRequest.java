package org.example.msclientes.application.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.msclientes.application.enums.ETipoDocumento;

@Getter
@Setter
public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de identificación es obligatorio")
    private ETipoDocumento tipoDocumento;

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 1, message = "El genero puede tomar los valores M o F")
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad debe ser un número positivo")
    private Integer edad;

    private String direccion;

    private String telefono;

    @NotBlank(message = "El id del cliente es obligatorio")
    private String idCliente;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
