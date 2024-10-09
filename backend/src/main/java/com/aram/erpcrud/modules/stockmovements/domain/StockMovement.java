package com.aram.erpcrud.modules.stockmovements.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "movimiento_inventario")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "responsable_id")
    private Long responsibleId;

    @OneToMany(mappedBy = "movement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockMovementProduct> stockMovementProducts;

    @ManyToOne
    @JoinColumn(name = "tipo_movimiento_inventario_id")
    private StockMovementType movementType;

    @Column(name = "observaciones")
    private String observations;

    @Column(name = "creado")
    private Instant timestamp;
}