package com.tp.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@MappedSuperclass
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public abstract class EntityId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

}