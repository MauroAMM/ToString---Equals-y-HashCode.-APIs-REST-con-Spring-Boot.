package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"facturas"})
@ToString(exclude = {"facturas"})
public class Cliente extends AuditoriaApp {

    private String denominacion;
    private String cuit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condicion_iva_id")
    private CondicionIva condicionIva;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "domicilio_id")
    private Domicilio domicilio;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contacto_id")
    private Contacto contacto;

    @OneToMany(mappedBy = "cliente")
    private List<FacturaVenta> facturas;
}