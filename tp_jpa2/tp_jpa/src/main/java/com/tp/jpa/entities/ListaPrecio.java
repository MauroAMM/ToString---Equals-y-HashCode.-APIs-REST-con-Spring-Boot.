package com.tp.jpa.entities;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lista_precio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"detalles"})
@ToString(exclude = {"detalles"})
public class ListaPrecio extends AuditoriaApp {

    private String codigo;
    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fechaDesde;

    @Temporal(TemporalType.DATE)
    private Date fechaHasta;

    private boolean activa;

    @OneToMany(mappedBy = "listaPrecio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListaPrecioArticulo> detalles;
}