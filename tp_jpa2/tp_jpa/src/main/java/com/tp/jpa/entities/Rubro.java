package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "rubro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class Rubro extends AuditoriaApp { // <-- CAMBIADO: extender de AuditoriaApp

    private String denominacion;
}