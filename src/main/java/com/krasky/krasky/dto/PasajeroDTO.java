package com.krasky.krasky.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PasajeroDTO {
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    // Validación estricta de DNI pedida en el PDF [cite: 198]
    @Pattern(regexp="^[0-9]{8}[A-Z]$", message = "DNI inválido")
    private String dni;

    @Email(message = "Formato de correo inválido")
    @NotBlank
    private String email;

    private String telefono;

    @Past(message = "La fecha debe ser pasada")
    @NotNull
    private LocalDate fechaNacimiento;
}