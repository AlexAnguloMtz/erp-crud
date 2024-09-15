package com.aram.erpcrud.personaldetails.domain;

import com.aram.erpcrud.locations.domain.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonalDetails {

    @Id
    private String id;

    @Column
    private String accountId;

    @Column
    private String name;

    @Column
    private String lastName;

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
    private String phone;

    @Column
    private String zipCode;

}