package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "factura_venta_detalle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"factura"})
@ToString(exclude = {"factura"})
public class FacturaVentaDetalle extends EntityId {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private FacturaVenta factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    private double cantidad;
    private double precioUnitario;
    private double subtotal;
}