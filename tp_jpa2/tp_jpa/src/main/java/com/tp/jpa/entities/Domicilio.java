package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "domicilio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class Domicilio extends EntityId {

    private String calle;
    private String numero;
    private String localidad;
    private String provincia;
}