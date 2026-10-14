package com.devgear.ms_notificaciones.listener;

import com.devgear.ms_notificaciones.config.RabbitMQConfig;
import com.devgear.ms_notificaciones.event.OrdenCreadaEvento;
import com.devgear.ms_notificaciones.service.BoletaHtml;
import com.devgear.ms_notificaciones.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrdenCreadaListener {

    private final EmailService emailService;

    public OrdenCreadaListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACIONES)
    public void onOrdenCreada(OrdenCreadaEvento evento) {
        try {
            String asunto = "Confirmación de tu compra #" + evento.ordenId();
            emailService.enviarCorreoHtml(evento.email(), asunto, BoletaHtml.texto(evento), BoletaHtml.html(evento));
        } catch (Exception ex) {
            System.err.println("No se pudo enviar el correo de la orden " + evento.ordenId() + ": " + ex.getMessage());
        }
    }
}
