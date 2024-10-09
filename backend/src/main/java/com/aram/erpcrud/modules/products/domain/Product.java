package com.aram.erpcrud.modules.products.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String name;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "categoria_producto_id")
    private ProductCategory productCategory;

}