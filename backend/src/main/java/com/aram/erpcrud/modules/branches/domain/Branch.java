package com.aram.erpcrud.modules.branches.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sucursal")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "telefono")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "domicilio_id")
    private BranchAddress address;

}