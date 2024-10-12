package com.aram.erpcrud.modules.branches.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_sucursal")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

}