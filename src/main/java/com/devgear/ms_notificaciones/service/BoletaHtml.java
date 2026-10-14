package com.devgear.ms_notificaciones.service;

import com.devgear.ms_notificaciones.event.OrdenCreadaEvento;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Arma la boleta de compra como HTML con CSS en línea (los clientes de correo ignoran
 * hojas de estilo externas) y una versión en texto plano de respaldo.
 */
public final class BoletaHtml {

    private static final ZoneId ZONA_CHILE = ZoneId.of("America/Santiago");
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm", Locale.ROOT);

    private BoletaHtml() {
    }

    public static String html(OrdenCreadaEvento evento) {
        StringBuilder filas = new StringBuilder();
        List<OrdenCreadaEvento.ItemEvento> items = evento.items() == null ? List.of() : evento.items();
        for (OrdenCreadaEvento.ItemEvento item : items) {
            BigDecimal subtotal = item.precioUnitario().multiply(BigDecimal.valueOf(item.cantidad()));
            filas.append("""
                <tr>
                  <td style="padding:14px 0;border-bottom:1px solid #e6e9f2;font-size:15px;color:#141a33;">
                    <strong style="font-weight:600;">%s</strong><br>
                    <span style="font-size:13px;color:#6b7390;">%d × %s</span>
                  </td>
                  <td align="right" style="padding:14px 0;border-bottom:1px solid #e6e9f2;font-size:15px;color:#141a33;white-space:nowrap;vertical-align:top;">%s</td>
                </tr>
                """.formatted(escapar(item.nombreProducto()), item.cantidad(), clp(item.precioUnitario()), clp(subtotal)));
        }

        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Confirmación de tu compra #%1$d</title>
            </head>
            <body style="margin:0;padding:0;background:#eef1f8;font-family:'Segoe UI',Helvetica,Arial,sans-serif;">
              <div style="display:none;max-height:0;overflow:hidden;opacity:0;">Recibimos tu orden #%1$d por %3$s. Gracias por comprar en DevGear Store.</div>
              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background:#eef1f8;padding:32px 12px;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="600" cellpadding="0" cellspacing="0" style="width:100%%;max-width:600px;background:#ffffff;border-radius:16px;overflow:hidden;border:1px solid #dde2f0;">

                      <tr>
                        <td style="background:#141a33;padding:28px 32px;">
                          <table role="presentation" cellpadding="0" cellspacing="0">
                            <tr>
                              <td style="background:#2f54ff;border-radius:8px;width:40px;height:40px;text-align:center;color:#ffffff;font-size:18px;font-weight:700;letter-spacing:0.5px;">DG</td>
                              <td style="padding-left:12px;color:#ffffff;font-size:20px;font-weight:700;letter-spacing:0.2px;">DevGear Store</td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <tr>
                        <td style="padding:36px 32px 8px 32px;">
                          <p style="margin:0 0 6px 0;font-size:13px;font-weight:600;letter-spacing:1.2px;text-transform:uppercase;color:#2f54ff;">Compra confirmada</p>
                          <h1 style="margin:0 0 10px 0;font-size:26px;line-height:1.25;color:#141a33;">¡Gracias por tu compra!</h1>
                          <p style="margin:0;font-size:15px;line-height:1.6;color:#4a5273;">Recibimos tu orden y ya estamos preparando tu despacho. Aquí tienes el detalle.</p>
                        </td>
                      </tr>

                      <tr>
                        <td style="padding:24px 32px 8px 32px;">
                          <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f6fd;border-radius:12px;">
                            <tr>
                              <td style="padding:16px 20px;">
                                <span style="font-size:12px;color:#6b7390;text-transform:uppercase;letter-spacing:1px;">Orden</span><br>
                                <strong style="font-size:18px;color:#141a33;">#%1$d</strong>
                              </td>
                              <td align="right" style="padding:16px 20px;">
                                <span style="font-size:12px;color:#6b7390;text-transform:uppercase;letter-spacing:1px;">Fecha</span><br>
                                <strong style="font-size:15px;color:#141a33;">%2$s</strong>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <tr>
                        <td style="padding:16px 32px 0 32px;">
                          <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                            <tr>
                              <td style="padding-bottom:8px;border-bottom:2px solid #141a33;font-size:12px;font-weight:600;letter-spacing:1px;text-transform:uppercase;color:#141a33;">Producto</td>
                              <td align="right" style="padding-bottom:8px;border-bottom:2px solid #141a33;font-size:12px;font-weight:600;letter-spacing:1px;text-transform:uppercase;color:#141a33;">Subtotal</td>
                            </tr>
            %4$s
                          </table>
                        </td>
                      </tr>

                      <tr>
                        <td style="padding:8px 32px 32px 32px;">
                          <table role="presentation" width="100%%" cellpadding="0" cellspacing="0">
                            <tr>
                              <td style="padding-top:18px;font-size:16px;font-weight:600;color:#141a33;">Total pagado</td>
                              <td align="right" style="padding-top:18px;font-size:26px;font-weight:700;color:#2f54ff;">%3$s</td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <tr>
                        <td style="background:#f4f6fd;padding:22px 32px;border-top:1px solid #e6e9f2;">
                          <p style="margin:0 0 4px 0;font-size:13px;color:#4a5273;">Puedes revisar tus compras en cualquier momento en la sección <strong>Mis órdenes</strong> de la tienda.</p>
                          <p style="margin:0;font-size:12px;color:#8b92ad;">DevGear Store · Este es un correo automático, no es necesario responderlo.</p>
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(evento.ordenId(), fecha(evento), clp(evento.total()), filas.toString());
    }

    /** Respaldo para clientes de correo que no muestran HTML. */
    public static String texto(OrdenCreadaEvento evento) {
        StringBuilder sb = new StringBuilder();
        sb.append("¡Gracias por tu compra en DevGear Store!\n\n");
        sb.append("Orden #").append(evento.ordenId()).append('\n');
        sb.append("Fecha: ").append(fecha(evento)).append("\n\n");
        sb.append("Detalle:\n");
        if (evento.items() != null) {
            for (OrdenCreadaEvento.ItemEvento item : evento.items()) {
                sb.append("- ").append(item.nombreProducto())
                  .append(" x").append(item.cantidad())
                  .append(" (").append(clp(item.precioUnitario())).append(" c/u)\n");
            }
        }
        sb.append("\nTotal: ").append(clp(evento.total()));
        return sb.toString();
    }

    private static String fecha(OrdenCreadaEvento evento) {
        if (evento.fecha() == null) {
            return "";
        }
        // El servicio guarda la fecha en UTC; se muestra en hora de Chile.
        return evento.fecha().atZone(ZoneOffset.UTC).withZoneSameInstant(ZONA_CHILE).format(FORMATO_FECHA);
    }

    /** 180990 → $180.990 (sin depender de los datos de locale del JRE). */
    static String clp(BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        long entero = valor.setScale(0, RoundingMode.HALF_UP).longValue();
        return "$" + String.format(Locale.ROOT, "%,d", entero).replace(',', '.');
    }

    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
