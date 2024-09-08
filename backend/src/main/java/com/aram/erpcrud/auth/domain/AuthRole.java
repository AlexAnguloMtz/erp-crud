package com.aram.erpcrud.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@Table(name = "roles")
public class AuthRole {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String canonicalName;

    @Column(nullable = false, unique = true)
    private String name;
}