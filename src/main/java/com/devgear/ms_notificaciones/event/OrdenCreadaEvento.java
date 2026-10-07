package com.devgear.ms_notificaciones.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenCreadaEvento(
    Long ordenId,
    String usuarioId,
    String email,
    LocalDateTime fecha,
    BigDecimal total,
    List<ItemEvento> items
) {
    public record ItemEvento(
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario
    ) {}
}