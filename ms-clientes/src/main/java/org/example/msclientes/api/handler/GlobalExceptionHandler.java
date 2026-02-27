package org.example.msclientes.api.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.example.msclientes.domain.exception.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNoEncontradoException ex, WebRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String mensaje = "Error en el formato del JSON";

        Throwable causa = ex.getCause();
        if (causa instanceof InvalidFormatException invalidFormatEx) {
            String campo = invalidFormatEx.getPath().isEmpty() ? "desconocido"
                    : invalidFormatEx.getPath().get(0).getFieldName();
            Class<?> tipoEsperado = invalidFormatEx.getTargetType();
            Object valorRecibido = invalidFormatEx.getValue();

            if (tipoEsperado == Integer.class || tipoEsperado == int.class) {
                mensaje = String.format("El campo '%s' debe ser un número entero. Valor recibido: '%s'", campo, valorRecibido);
            } else if (tipoEsperado == Boolean.class || tipoEsperado == boolean.class) {
                mensaje = String.format("El campo '%s' debe ser un valor booleano (true/false). Valor recibido: '%s'", campo, valorRecibido);
            } else if (tipoEsperado.isEnum()) {
                mensaje = String.format("El campo '%s' tiene un valor inválido: '%s'. Valores permitidos: %s",
                        campo, valorRecibido, java.util.Arrays.toString(tipoEsperado.getEnumConstants()));
            } else {
                mensaje = String.format("El campo '%s' tiene un tipo de dato inválido. Valor recibido: '%s'", campo, valorRecibido);
            }
        }

        log.warn("Error de deserialización JSON: {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, mensaje, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            errores.put(campo, error.getDefaultMessage());
        });
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Errores de validación")
                .message(errores.toString())
                .path(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, WebRequest request) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request));
    }

    private ErrorResponse buildError(HttpStatus status, String message, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false))
                .build();
    }
}
