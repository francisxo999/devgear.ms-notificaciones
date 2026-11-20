package com.devgear.ms_notificaciones.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Copia propia del contrato que publica ms-ordenes (no se comparte código entre servicios).
// Se ignoran los campos desconocidos para que un campo nuevo en ms-ordenes no rompa a este consumidor.
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrdenCreadaEvento(
    Long ordenId,
    String usuarioId,
    String email,
    LocalDateTime fecha,
    BigDecimal total,
    String metodoPago,
    List<ItemEvento> items
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ItemEvento(
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario,
        String imagenUrl
    ) {}
}
