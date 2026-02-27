package org.example.msclientes.infraestructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.msclientes.application.dto.ClienteEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(ClienteEvent event) {
        log.info("Publicando evento [{}] para clienteId: {}", event.getEventType(), event.getClienteId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_CLIENTE,
                event
        );
    }
}
