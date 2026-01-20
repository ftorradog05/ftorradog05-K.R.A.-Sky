package com.krasky.krasky.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AvionDTO {
    private Long id;
    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @Min(value = 1, message = "La capacidad turista debe ser al menos 1")
    private int capacidadTurista;

    @Min(value = 0, message = "La capacidad business no puede ser negativa")
    private int capacidadBusiness;
}