package com.aram.erpcrud.movements.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "stock_movement_product")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class StockMovementProduct {

    @Id
    private UUID id;

    @Column
    private UUID productId;

    @Column
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "stock_movement_id")
    private Movement movement;

}