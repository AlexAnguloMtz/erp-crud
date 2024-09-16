package com.aram.erpcrud.movements.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_quantity")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class ProductQuantity {

    @Id
    private String id;

    @Column
    private String productId;

    @Column
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "movement_id")
    private Movement movement;

}