package org.example.mscuentas.infrastructure.messaging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mscuentas.application.dto.ClienteEvent;
import org.example.mscuentas.application.enums.ETipoEvento;
import org.example.mscuentas.domain.model.ClientesInfo;
import org.example.mscuentas.domain.repository.ClienteRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventConsumer {

    private final ClienteRepository clienteInfoRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CLIENTES)
    public void onClienteEvent(ClienteEvent event) {
        log.info("Evento recibido [{}] para clienteId: {}", event.getEventType(), event.getClienteId());

        switch (event.getEventType()) {
            case "CREATED", "UPDATED" -> upsertClienteInfo(event);
            case "DELETED"            -> clienteInfoRepository.deleteById(event.getId());
            default -> log.warn("Tipo de evento desconocido: {}", event.getEventType());
        }
    }

    private void upsertClienteInfo(ClienteEvent event) {
        ClientesInfo info = clienteInfoRepository.findByClienteId(event.getClienteId())
                .orElseGet(ClientesInfo::new);
        info.setId((ETipoEvento.isValidCodigo(ETipoEvento.CREATED, event.getEventType()) ? null : event.getId()));
        info.setClienteId(event.getClienteId());
        info.setNombre(event.getNombre());
        info.setIdentificacion(event.getIdentificacion());
        info.setEstado(event.getEstado());
        clienteInfoRepository.save(info);
    }
}
