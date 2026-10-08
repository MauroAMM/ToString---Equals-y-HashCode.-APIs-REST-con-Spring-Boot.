package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "lista_precio_articulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"listaPrecio"})
@ToString(exclude = {"listaPrecio"})
public class ListaPrecioArticulo extends EntityId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_precio_id", nullable = false)
    private ListaPrecio listaPrecio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    private double precio;
}