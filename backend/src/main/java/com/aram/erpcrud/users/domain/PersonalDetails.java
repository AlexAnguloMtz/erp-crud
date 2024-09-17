package com.aram.erpcrud.users.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_personal_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PersonalDetails {

    @Id
    private UUID id;

    @Column
    private UUID accountId;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private UserAddress address;

}