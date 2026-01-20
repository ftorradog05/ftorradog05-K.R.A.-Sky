package com.krasky.krasky.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vuelos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_vuelo", nullable = false, unique = true, length = 20)
    private String numeroVuelo;

    @Column(nullable = false, length = 100)
    private String origen;

    @Column(nullable = false, length = 100)
    private String destino;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_llegada", nullable = false)
    private LocalDateTime fechaLlegada;

    // Usamos BigDecimal para dinero (es más preciso que double)
    @Column(name = "precio_turista", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTurista;

    @Column(name = "precio_business", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBusiness;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVuelo estado;

    // --- RELACIÓN CON AVIÓN ---
    // Un vuelo tiene un avión asignado.
    // Esto une la columna 'avion_id' de la tabla 'vuelos' con la entidad 'Avion'.
    @ManyToOne
    @JoinColumn(name = "avion_id", nullable = false)
    private Avion avion;
}