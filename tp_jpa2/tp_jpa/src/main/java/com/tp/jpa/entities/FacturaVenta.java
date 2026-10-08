package com.tp.jpa.entities;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "factura_venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"detalles"})
@ToString(exclude = {"detalles"})
public class FacturaVenta extends AuditoriaApp {

    private Long numero;

    @Column(name = "fechaEmision", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicion_iva_id", nullable = false)
    private CondicionIva condicionIva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_moneda_id", nullable = false)
    private TipoMoneda tipoMoneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punto_venta_id", nullable = false)
    private PuntoVenta puntoVenta;

    @Column(name = "importeCobrado")
    private double importeCobrado;

    @Column(name = "importeSaldo")
    private double importeSaldo;

    @Column(name = "importeTotal", nullable = false)
    private double importeTotal;

    private String cae;

    @Column(name = "caeFechaVencimiento")
    @Temporal(TemporalType.DATE)
    private Date caeFechaVencimiento;

    @Column(name = "resultadoAfip")
    private String resultadoAfip;

    @Column(name = "motivoRechazo")
    private String motivoRechazo;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fechaAnulacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulacion;

    private String observaciones;

    @Builder.Default
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaVentaDetalle> detalles = new ArrayList<>();
}