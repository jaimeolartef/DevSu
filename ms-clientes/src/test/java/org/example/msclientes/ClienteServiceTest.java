package org.example.msclientes;


import org.example.msclientes.application.dto.ClienteRequest;
import org.example.msclientes.application.dto.ClienteResponse;
import org.example.msclientes.application.enums.ETipoDocumento;
import org.example.msclientes.application.service.ClienteService;
import org.example.msclientes.domain.exception.ClienteNotFoundException;
import org.example.msclientes.domain.model.Cliente;
import org.example.msclientes.domain.repository.ClienteRepository;
import org.example.msclientes.infraestructure.messaging.ClienteEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Pruebas Unitarias")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteEventPublisher eventPublisher;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteExistente;

    @BeforeEach
    void setUp() {
        clienteExistente = new Cliente();
        clienteExistente.setNombre("Jose Lema");
        clienteExistente.setGenero("Masculino");
        clienteExistente.setEdad(35);
        clienteExistente.setIdentificacion("12345678");
        clienteExistente.setDireccion("Otavalo");
        clienteExistente.setTelefono("098254785");
        clienteExistente.setClienteId("CLI001");
        clienteExistente.setContrasena("1234");
        clienteExistente.setEstado(true);
    }

    @Test
    @DisplayName("Debe retornar lista de clientes")
    void findAll_debeRetornarLista() {
        when(clienteRepository.findAll()).thenReturn(List.of(clienteExistente));

        List<ClienteResponse> result = clienteService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Jose Lema");
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar cliente por ID")
    void findById_cuandoExiste_debeRetornarCliente() {
        when(clienteRepository.findByClienteId("CLI001")).thenReturn(Optional.of(clienteExistente));

        ClienteResponse result = clienteService.findByIdCliente("CLI001");

        assertThat(result.getNombre()).isEqualTo("Jose Lema");
        assertThat(result.getClienteId()).isEqualTo("CLI001");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el cliente no existe")
    void findById_cuandoNoExiste_debeLanzarExcepcion() {
        when(clienteRepository.findByClienteId("TEST")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.findByIdCliente("TEST"))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado con clienteId: TEST");
    }

    @Test
    @DisplayName("Debe crear un cliente correctamente y publicar evento")
    void create_debeGuardarClienteYPublicarEvento() {
        ClienteRequest request = buildRequest();
        when(clienteRepository.findByIdentificacion(request.getIdentificacion())).thenReturn(Optional.empty());
        when(clienteRepository.findByClienteId(request.getIdCliente())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteExistente);

        ClienteResponse result = clienteService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Jose Lema");
        verify(clienteRepository).save(any(Cliente.class));
        verify(eventPublisher).publish(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el clienteId ya existe")
    void create_cuandoClienteIdDuplicado_debeLanzarExcepcion() {
        ClienteRequest request = buildRequest();
        when(clienteRepository.findByClienteId(request.getIdCliente())).thenReturn(Optional.of(clienteExistente));

        assertThatThrownBy(() -> clienteService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CLI001");
    }

    @Test
    @DisplayName("Debe eliminar un cliente y publicar evento DELETED")
    void delete_cuandoExiste_debeEliminarYPublicarEvento() {
        when(clienteRepository.findByClienteId("CLI001")).thenReturn(Optional.of(clienteExistente));

        clienteService.delete("CLI001");

        verify(clienteRepository).delete(clienteExistente);
        verify(eventPublisher).publish(argThat(e -> "DELETED".equals(e.getEventType())));
    }

    private ClienteRequest buildRequest() {
        ClienteRequest r = new ClienteRequest();
        r.setNombre("Jose Lema");
        r.setGenero("Masculino");
        r.setEdad(35);
        r.setTipoDocumento(ETipoDocumento.CC);
        r.setIdentificacion("12345678");
        r.setDireccion("Otavalo");
        r.setTelefono("098254785");
        r.setIdCliente("CLI001");
        r.setContrasena("1234");
        r.setEstado(true);
        return r;
    }
}
