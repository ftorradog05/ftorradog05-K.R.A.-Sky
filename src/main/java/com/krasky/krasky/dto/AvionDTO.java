package com.krasky.krasky.dto;

import lombok.Data;

@Data
public class AvionDTO {
    private Long id;
    private String matricula;
    private String modelo;
    private int capacidadTurista;
    private int capacidadBusiness;
}