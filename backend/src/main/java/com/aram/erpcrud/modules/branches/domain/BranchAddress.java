package com.aram.erpcrud.modules.branches.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domicilio_sucursal")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class BranchAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "colonia")
    private String district;

    @Column(name = "calle")
    private String street;

    @Column(name = "numero_calle")
    private String streetNumber;

    @Column(name = "codigo_postal")
    private String zipCode;

}