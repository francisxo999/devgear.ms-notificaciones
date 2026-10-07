package com.devgear.ms_notificaciones.listener;

import com.devgear.ms_notificaciones.config.RabbitMQConfig;
import com.devgear.ms_notificaciones.event.OrdenCreadaEvento;
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
            String cuerpo = construirCuerpoCorreo(evento);
            emailService.enviarCorreo(evento.email(), asunto, cuerpo);
        } catch (Exception ex) {
            // No relanzamos -- igual que en ms-productos, evitamos que RabbitMQ
            // reintente en loop. Si falla el envío, queda logeado nada más.
            System.err.println("No se pudo enviar el correo de la orden " + evento.ordenId() + ": " + ex.getMessage());
        }
    }

    private String construirCuerpoCorreo(OrdenCreadaEvento evento) {
        StringBuilder sb = new StringBuilder();
        sb.append("¡Gracias por tu compra en DevGear Store!\n\n");
        sb.append("Orden #").append(evento.ordenId()).append("\n");
        sb.append("Fecha: ").append(evento.fecha()).append("\n\n");
        sb.append("Detalle:\n");
        for (OrdenCreadaEvento.ItemEvento item : evento.items()) {
            sb.append("- ").append(item.nombreProducto())
              .append(" x").append(item.cantidad())
              .append(" ($").append(item.precioUnitario()).append(" c/u)\n");
        }
        sb.append("\nTotal: $").append(evento.total());
        return sb.toString();
    }
}