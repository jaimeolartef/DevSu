package org.example.msclientes.domain.exception;

public class ClienteNotFoundException extends RecursoNoEncontradoException {
    public ClienteNotFoundException(Long id) {
        super("Cliente no encontrado con id: " + id);
    }

    public ClienteNotFoundException(String clienteId) {
        super("Cliente no encontrado con clienteId: " + clienteId);
    }
}
