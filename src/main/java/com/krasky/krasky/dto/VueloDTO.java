package com.krasky.krasky.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VueloDTO {
    private Long id;
    private String numeroVuelo;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private BigDecimal precioTurista;
    private BigDecimal precioBusiness;
    private String estado;

    // Solo necesitamos el ID para conectar el vuelo con un avión existente
    private Long avionId;
}