package com.aram.erpcrud.modules.stockmovements.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movimiento_inventario_producto")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class StockMovementProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productId;

    @Column(name = "cantidad")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "movimiento_inventario_id")
    private StockMovement movement;

}