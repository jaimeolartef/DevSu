package org.example.msclientes.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.msclientes.application.dto.ClienteRequest;
import org.example.msclientes.application.dto.ClienteResponse;
import org.example.msclientes.application.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name        = "Clientes",
    description = "Operaciones CRUD sobre la entidad Cliente"
)
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponse> findById(@PathVariable String idCliente) {
        return ResponseEntity.ok(clienteService.findByIdCliente(idCliente));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(request));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteResponse> update(@PathVariable String idCliente,
                                                  @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.update(idCliente, request));
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> delete(@PathVariable String idCliente) {
        clienteService.delete(idCliente);
        return ResponseEntity.noContent().build();
    }
}
