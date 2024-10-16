package com.aram.erpcrud.modules.authorization.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuenta")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Role role;

    @Column(name = "correo")
    private String email;

    @Column(name = "contrasena")
    private String password;

}