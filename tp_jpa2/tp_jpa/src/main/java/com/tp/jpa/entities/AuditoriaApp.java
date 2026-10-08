package com.tp.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class AuditoriaApp extends EntityId {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_alta", nullable = false, updatable = false)
    private Date fechaAlta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    @Column(name = "usuario_alta")
    private String usuarioAlta;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
}