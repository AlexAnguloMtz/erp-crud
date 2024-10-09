package com.aram.erpcrud.modules.users.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuenta")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private UserRole role;

    @Column(name = "correo")
    private String email;

}