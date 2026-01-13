package com.krasky.krasky.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PasajeroDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
}