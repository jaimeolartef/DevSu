package org.example.mscuentas.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE       = "banco.exchange";
    public static final String QUEUE_CLIENTES = "clientes.queue";
    public static final String ROUTING_KEY    = "cliente.evento";

    @Bean
    public TopicExchange bancoExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue clientesQueue() {
        return new Queue(QUEUE_CLIENTES, true);
    }

    @Bean
    public Binding clientesBinding(Queue clientesQueue, TopicExchange bancoExchange) {
        return BindingBuilder.bind(clientesQueue).to(bancoExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
