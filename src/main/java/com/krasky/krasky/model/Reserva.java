package com.krasky.krasky.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_reserva", nullable = false, unique = true, length = 50)
    private String codigoReserva;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(nullable = false, length = 20)
    private String clase; // EJ: "TURISTA", "BUSINESS"

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(nullable = false, length = 20)
    private String estado; // EJ: "CONFIRMADA", "CANCELADA"

    @Column(length = 10)
    private String asiento;

    // --- RELACIONES ---

    // Una reserva pertenece a un Vuelo específico
    @ManyToOne
    @JoinColumn(name = "vuelo_id", nullable = false)
    private Vuelo vuelo;

    // Una reserva pertenece a un Pasajero específico
    @ManyToOne
    @JoinColumn(name = "pasajero_id", nullable = false)
    private Pasajero pasajero;
}