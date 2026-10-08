package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "articulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class Articulo extends AuditoriaApp {

    private String codigo;
    private String denominacion;
    private double precioVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rubro_id")
    private Rubro rubro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;
}