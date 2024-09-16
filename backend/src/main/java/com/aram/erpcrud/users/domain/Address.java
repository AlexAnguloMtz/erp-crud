package com.aram.erpcrud.users.domain;

import com.aram.erpcrud.locations.domain.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Address {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

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
