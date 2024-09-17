package com.aram.erpcrud.users.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_address")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class UserAddress {

    @Id
    private UUID id;

    @Column
    private String stateId;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String street;

    @Column
    private String streetNumber;

    @Column
    private String zipCode;

}
