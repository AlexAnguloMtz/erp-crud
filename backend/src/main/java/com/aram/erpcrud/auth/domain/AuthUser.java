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
@Table(name = "auth_user")
public class AuthUser {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "auth_role_id")
    private AuthRole role;

    @Column
    private String email;

    @Column
    private String password;

}