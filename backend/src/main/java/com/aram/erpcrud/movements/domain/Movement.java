package com.aram.erpcrud.movements.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stock_movement")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Movement {

    @Id
    private UUID id;

    @Column
    private UUID responsibleId;

    @OneToMany(mappedBy = "movement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockMovementProduct> stockMovementProducts;

    @ManyToOne
    @JoinColumn(name = "stock_movement_type_id")
    private MovementType movementType;

    @Column
    private String observations;

    @Column
    private Instant timestamp;
}
