package com.aram.erpcrud.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    @Column
    private String state;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String streetNumber;

    @Column
    private String phone;

    @Column
    private String zipCode;

}