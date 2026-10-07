package com.devgear.ms_notificaciones.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EnviarCorreoRequestDTO(
    @NotBlank @Email String to,
    @NotBlank String subject,
    @NotBlank String body
) {}