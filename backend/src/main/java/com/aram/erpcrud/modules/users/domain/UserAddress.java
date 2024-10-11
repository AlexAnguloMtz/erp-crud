package com.aram.erpcrud.modules.users.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domicilio_personal")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class UserAddress {

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