package com.aram.erpcrud.modules.products.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto")
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "precio_venta_centavos")
    private Integer salePrice;

    @Column(name = "sku")
    private String sku;

    @Column(name = "imagen")
    private String image;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "categoria_producto_id")
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "unidad_inventario_id")
    private InventoryUnit inventoryUnit;

}