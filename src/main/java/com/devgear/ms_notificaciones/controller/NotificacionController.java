package com.devgear.ms_notificaciones.controller;

import com.devgear.ms_notificaciones.dto.EnviarCorreoRequestDTO;
import com.devgear.ms_notificaciones.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint para disparar un correo manualmente, sin pasar por RabbitMQ.
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final EmailService emailService;

    public NotificacionController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreoPrueba(@Valid @RequestBody EnviarCorreoRequestDTO request) {
        try {
            emailService.enviarCorreo(request.to(), request.subject(), request.body());
            return ResponseEntity.ok("Correo enviado exitosamente a " + request.to());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Error al enviar el correo: " + e.getMessage());
        }
    }
}