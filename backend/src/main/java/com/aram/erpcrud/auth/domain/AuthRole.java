package com.aram.erpcrud.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@Table(name = "auth_role")
public class AuthRole {

    @Id
    private UUID id;

    @Column
    private String canonicalName;

    @Column
    private String name;

}