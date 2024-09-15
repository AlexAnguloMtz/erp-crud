package com.aram.erpcrud.movements.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "movements")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Movement {

    @Id
    private String id;

    @Column
    private String responsibleId;

    @OneToMany(mappedBy = "movement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductQuantity> productQuantities;

    @ManyToOne
    private MovementType movementType;

    @Column
    private String observations;

    @Column
    private Instant timestamp;
}
