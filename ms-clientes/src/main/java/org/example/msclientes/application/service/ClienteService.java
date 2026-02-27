package org.example.msclientes.application.service;


import lombok.RequiredArgsConstructor;
import org.example.msclientes.application.dto.ClienteEvent;
import org.example.msclientes.application.dto.ClienteRequest;
import org.example.msclientes.application.dto.ClienteResponse;
import org.example.msclientes.application.enums.ETipoEvento;
import org.example.msclientes.domain.exception.ClienteNotFoundException;
import org.example.msclientes.domain.model.Cliente;
import org.example.msclientes.domain.repository.ClienteRepository;
import org.example.msclientes.infraestructure.messaging.ClienteEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ClienteResponse> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse findByIdCliente(String idCliente) {
        Cliente cliente = clienteRepository.findByClienteId(idCliente)
                .orElseThrow(() -> new ClienteNotFoundException(idCliente));
        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse create(ClienteRequest request) {
        if (clienteRepository.findByIdentificacion(request.getIdentificacion()).isPresent()) {
             throw new IllegalArgumentException("Ya existe un cliente con identificacion: " + request.getIdentificacion());
        }

        if (clienteRepository.findByClienteId(request.getIdCliente()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con id: " + request.getIdCliente());
        }

        Cliente cliente = toEntity(request);
        Cliente saved = clienteRepository.save(cliente);

        eventPublisher.publish(ClienteEvent.builder()
                .eventType(ETipoEvento.CREATED.getCodigo())
                .id(saved.getId())
                .clienteId(saved.getClienteId())
                .nombre(saved.getNombre())
                .identificacion(saved.getIdentificacion())
                .estado(saved.getEstado())
                .build());

        return toResponse(saved);
    }

    @Transactional
    public ClienteResponse update(String idCliente, ClienteRequest request) {
        Cliente cliente = clienteRepository.findByClienteId(idCliente)
                .orElseThrow(() -> new ClienteNotFoundException(idCliente));

        if (clienteRepository.findByIdentificacion(request.getIdentificacion()).isPresent()
            && !cliente.getClienteId().equals(request.getIdCliente())) {
            throw new IllegalArgumentException("Ya existe un cliente con identificacion: " + request.getIdentificacion());
        }

        if (clienteRepository.findByClienteId(request.getIdCliente()).isPresent()
            && !cliente.getClienteId().equals(request.getIdCliente())) {
            throw new IllegalArgumentException("Ya existe un cliente con id: " + request.getIdCliente());
        }

        cliente.setId(cliente.getId());
        setterCliente(cliente, request);

        Cliente saved = clienteRepository.save(cliente);

        eventPublisher.publish(ClienteEvent.builder()
                .eventType(ETipoEvento.UPDATED.getCodigo())
                .id(saved.getId())
                .clienteId(saved.getClienteId())
                .nombre(saved.getNombre())
                .identificacion(saved.getIdentificacion())
                .estado(saved.getEstado())
                .build());

        return toResponse(saved);
    }

    @Transactional
    public void delete(String idCliente) {
        Cliente cliente = clienteRepository.findByClienteId(idCliente)
                .orElseThrow(() -> new ClienteNotFoundException(idCliente));

        clienteRepository.delete(cliente);

        eventPublisher.publish(ClienteEvent.builder()
                .eventType("DELETED")
                .id(cliente.getId())
                .clienteId(cliente.getClienteId())
                .build());
    }

    private Cliente toEntity(ClienteRequest request) {
        Cliente cliente = new Cliente();
        setterCliente(cliente, request);
        return cliente;
    }

    private void setterCliente(Cliente cliente, ClienteRequest request) {
        cliente.setNombre(request.getNombre());
        cliente.setTipoDocumento(request.getTipoDocumento().getCodigo());
        cliente.setGenero(request.getGenero());
        cliente.setEdad(request.getEdad());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());
        cliente.setClienteId(request.getIdCliente());
        cliente.setContrasena(request.getContrasena());
        cliente.setEstado(request.getEstado());
    }

    private ClienteResponse toResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .genero(c.getGenero())
                .edad(c.getEdad())
                .tipoDocumento(c.getTipoDocumento())
                .identificacion(c.getIdentificacion())
                .direccion(c.getDireccion())
                .telefono(c.getTelefono())
                .clienteId(c.getClienteId())
                .estado(c.getEstado())
                .build();
    }
}
