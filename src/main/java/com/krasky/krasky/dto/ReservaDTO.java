package com.krasky.krasky.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaDTO {
    private Long id;
    private String codigoReserva;
    private LocalDateTime fechaReserva;
    private String clase;
    private BigDecimal precioTotal;
    private String estado;
    private String asiento;

    // Relaciones (Solo IDs)
    private Long vueloId;
    private Long pasajeroId;
}