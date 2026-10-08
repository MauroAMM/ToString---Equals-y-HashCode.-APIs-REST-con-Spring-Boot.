package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_moneda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class TipoMoneda extends EntityId {

    private String codigo;
    private String descripcion;
    private double cotizacion;
}