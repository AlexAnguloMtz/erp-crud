package com.aram.erpcrud.users.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personal_details")
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

    @Column
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private UserAddress address;

}