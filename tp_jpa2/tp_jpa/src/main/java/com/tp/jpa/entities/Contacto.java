package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString
public class Contacto extends EntityId {

    private String telefono;
    private String email;
}