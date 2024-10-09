package com.aram.erpcrud.modules.personaldetails.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domicilio")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class PersonalAddress {

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