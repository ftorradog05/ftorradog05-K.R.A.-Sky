package com.krasky.krasky.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "aviones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(name = "capacidad_turista", nullable = false)
    private int capacidadTurista;

    @Column(name = "capacidad_business", nullable = false)
    private int capacidadBusiness;
}
