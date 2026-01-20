package com.krasky.krasky.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaDTO {
    private Long id;
    private String codigoReserva;
    private LocalDateTime fechaReserva;
    @NotNull(message = "La clase es obligatoria (TURISTA/BUSINESS)")
    private String clase;
    private BigDecimal precioTotal;
    private String estado;
    private String asiento;

    @NotNull(message = "El ID del vuelo es obligatorio")
    private Long vueloId;

    @NotNull(message = "El ID del pasajero es obligatorio")
    private Long pasajeroId;

    private String vueloNumero;
    private String pasajeroNombre;
}