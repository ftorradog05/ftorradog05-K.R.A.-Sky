package com.krasky.krasky.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VueloDTO {
    private Long id;
    @NotBlank(message = "El número de vuelo es obligatorio")
    private String numeroVuelo;

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotNull(message = "La fecha de salida es obligatoria")
    @Future(message = "La fecha de salida debe ser en el futuro")
    private LocalDateTime fechaSalida;

    @NotNull(message = "La fecha de llegada es obligatoria")
    @Future(message = "La fecha de llegada debe ser en el futuro")
    private LocalDateTime fechaLlegada;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioTurista;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioBusiness;

    private String estado; // Se puede dejar como String en el DTO o usar el Enum

    @NotNull(message = "Debes asignar un avión")
    private Long avionId;

    private String avionModelo;
}