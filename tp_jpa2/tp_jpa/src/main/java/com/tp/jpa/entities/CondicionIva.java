package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "condicion_iva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class CondicionIva extends EntityId {

    private String codigo;
    private String denominacion;
    private double porcentaje;
}