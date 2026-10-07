package com.devgear.ms_notificaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_ORDEN_CREADA = "orden.creada.exchange";
    public static final String QUEUE_NOTIFICACIONES = "notificaciones.orden-creada.queue";

    @Bean
    public FanoutExchange ordenCreadaExchange() {
        return new FanoutExchange(EXCHANGE_ORDEN_CREADA);
    }

    @Bean
    public Queue notificacionesQueue() {
        return new Queue(QUEUE_NOTIFICACIONES, true);
    }

    @Bean
    public Binding notificacionesBinding(Queue notificacionesQueue, FanoutExchange ordenCreadaExchange) {
        return BindingBuilder.bind(notificacionesQueue).to(ordenCreadaExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}