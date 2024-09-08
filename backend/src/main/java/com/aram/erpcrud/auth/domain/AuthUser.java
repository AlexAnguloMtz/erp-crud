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
@Table(name = "users")
public class AuthUser {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private AuthRole role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}